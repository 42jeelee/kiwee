package kr.co.jeelee.kiwee.domain.content.repository;

import static kr.co.jeelee.kiwee.domain.content.entity.QContent.*;
import static kr.co.jeelee.kiwee.domain.contentMember.entity.QContentMember.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentReactSummary;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.domain.genre.entity.QGenre;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentQueryRepositoryImpl implements ContentQueryRepository {

	private final JPAQueryFactory query;

	@Override
	public Page<Content> findOrderByContentMemberUpdatedAt(Set<ContentType> contentTypes, Set<Long> genreIds, Pageable pageable) {
		Set<ContentType> safeTypes = contentTypes == null ? Set.of()
			: contentTypes.stream().filter(Objects::nonNull).collect(Collectors.toUnmodifiableSet());
		Set<Long> safeGenreIds = genreIds == null ? Set.of()
			: genreIds.stream().filter(Objects::nonNull).collect(Collectors.toUnmodifiableSet());

		BooleanBuilder where = new BooleanBuilder();
		if (!safeTypes.isEmpty()) {
			where.and(content.contentType.in(safeTypes));
		}

		var lastUpdatedAt = contentMember.updatedAt.max();

		QGenre genre = QGenre.genre;
		List<UUID> pageIds = query
			.select(content.id)
			.from(content)
			.leftJoin(contentMember).on(contentMember.content.eq(content))
			.leftJoin(content.genres, genre)
			.where(
				where
					.and(safeGenreIds.isEmpty() ? null : genre.id.in(safeGenreIds))
			)
			.groupBy(content.id)
			.orderBy(lastUpdatedAt.desc().nullsLast(), content.id.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		if (pageIds.isEmpty()) {
			Long total0 = query
				.select(content.id.countDistinct())
				.from(content)
				.leftJoin(content.genres, genre)
				.where(
					where
						.and(safeGenreIds.isEmpty() ? null : genre.id.in(safeGenreIds))
				)
				.fetchOne();
			return new PageImpl<>(List.of(), pageable, total0 == null ? 0 : total0);
		}

		OrderSpecifier<?> orderByIn = orderByIdsCase(pageIds);

		List<Content> contents = query
			.selectFrom(content)
			.where(content.id.in(pageIds))
			.orderBy(orderByIn)
			.fetch();

		Long total = query
			.select(content.id.countDistinct())
			.from(content)
			.leftJoin(content.genres, genre)
			.where(
				where
					.and(safeGenreIds.isEmpty() ? null : genre.id.in(safeGenreIds))
			)
			.fetchOne();

		return new PageImpl<>(contents, pageable, total == null ? 0 : total);
	}

	@Override
	public ContentReactSummary findReactSummaryById(UUID id) {
		if (id == null) return null;

		var lastUpdMax = contentMember.updatedAt.max();

		return query
			.select(createReactSummary(lastUpdMax))
			.from(content)
			.leftJoin(contentMember).on(contentMember.content.eq(content))
			.where(content.id.eq(id))
			.groupBy(content.id)
			.fetchOne();
	}

	@Override
	public List<ContentReactSummary> findReactSummaryByIds(List<UUID> ids) {
		if (ids == null || ids.isEmpty()) return List.of();

		var lastUpdMax = contentMember.updatedAt.max();

		return query
			.select(createReactSummary(lastUpdMax))
			.from(content)
			.leftJoin(contentMember).on(contentMember.content.eq(content))
			.where(content.id.in(ids))
			.groupBy(content.id)
			.fetch();
	}

	private OrderSpecifier<?> orderByIdsCase(List<UUID> pageIds) {
		if (pageIds == null || pageIds.isEmpty()) {
			return content.id.asc();
		}

		CaseBuilder.Cases<Integer, NumberExpression<Integer>> cases =
			new CaseBuilder().when(content.id.eq(pageIds.get(0))).then(0);

		for (int i = 1; i < pageIds.size(); i++) {
			cases = cases.when(content.id.eq(pageIds.get(i))).then(i);
		}

		NumberExpression<Integer> orderExpr = cases.otherwise(999_999);

		return orderExpr.asc();
	}

	private FactoryExpression<ContentReactSummary> createReactSummary(
		DateTimeExpression<LocalDateTime> lastUpdateExpr
	) {
		var countRecN2 = new CaseBuilder().when(contentMember.recommended.eq(-2)).then(1L).otherwise(0L).sum();
		var countRecN1 = new CaseBuilder().when(contentMember.recommended.eq(-1)).then(1L).otherwise(0L).sum();
		var countRec0  = new CaseBuilder().when(contentMember.recommended.eq(0)). then(1L).otherwise(0L).sum();
		var countRecP1 = new CaseBuilder().when(contentMember.recommended.eq(1)). then(1L).otherwise(0L).sum();
		var countRecP2 = new CaseBuilder().when(contentMember.recommended.eq(2)). then(1L).otherwise(0L).sum();
		var starAvg    = contentMember.star.avg().coalesce(0.0);
		var starCount  = contentMember.star.count().coalesce(0L);

		return Projections.constructor(
			ContentReactSummary.class,
			content.id,
			countRecN2, countRecN1, countRec0, countRecP1, countRecP2,
			starAvg, starCount,
			lastUpdateExpr
		);
	}

}
