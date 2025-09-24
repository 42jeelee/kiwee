package kr.co.jeelee.kiwee.domain.content.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentReactSummary;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;

public interface ContentQueryRepository {

	Page<Content> findOrderByContentMemberUpdatedAt(Set<ContentType> contentTypes, Set<Long> genreIds, Pageable pageable);

	ContentReactSummary findReactSummaryById(UUID id);

	List<ContentReactSummary> findReactSummaryByIds(List<UUID> ids);

}
