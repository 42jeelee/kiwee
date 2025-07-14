package kr.co.jeelee.kiwee.domain.content.service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.content.dto.request.ContentCreateRequest;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentDetailResponse;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentSimpleResponse;
import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.content.exception.ContentNotFoundException;
import kr.co.jeelee.kiwee.global.model.DataProvider;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.domain.content.repository.ContentRepository;
import kr.co.jeelee.kiwee.domain.genre.entity.Genre;
import kr.co.jeelee.kiwee.domain.genre.service.GenreService;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;
import kr.co.jeelee.kiwee.domain.platform.service.PlatformService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentServiceImpl implements ContentService {

	private final ContentRepository contentRepository;

	private final GenreService genreService;
	private final PlatformService platformService;

	@Override
	@Transactional
	public ContentDetailResponse createContent(ContentCreateRequest request) {

		if (!request.dataProvider().equals(DataProvider.NONE) && request.sourceId() == null) {
			throw new FieldValidationException("sourceId", "dataProvider가 있는 경우 반드시 있어야 합니다.");
		}

		if (request.contentLevel().getDepth() > 1 && request.parentId() == null) {
			throw new FieldValidationException("parentId", "contentLevel이 ROOT가 아니라면 부모 컨텐츠가 있어야 합니다.");
		}

		Set<Genre> genres = request.genres().stream()
			.map(genreService::getOrCreateGenre)
			.collect(Collectors.toSet());

		Set<Platform> platforms = request.platformIds().stream()
			.map(platformService::getById)
			.collect(Collectors.toSet());

		Content parent = request.parentId() != null
			? getById(request.parentId())
			: null;

		Content content = Content.of(
			request.dataProvider(),
			request.sourceId(),
			request.title(),
			request.originalTitle(),
			request.description(),
			request.rating() != null? request.rating() : 0.0D,
			request.imageUrl(),
			request.homePage(),
			request.contentType(),
			request.contentLevel(),
			parent,
			genres,
			platforms
		);

		return ContentDetailResponse.from(contentRepository.save(content));
	}

	@Override
	public ContentDetailResponse getContentDetail(UUID id) {
		return ContentDetailResponse.from(getById(id));
	}

	@Override
	public PagedResponse<ContentSimpleResponse> getContents(ContentType contentType, Set<Long> genreIds,
		Pageable pageable) {
		return PagedResponse.of(
			fetchContents(contentType, genreIds, pageable),
			ContentSimpleResponse::from
		);
	}

	@Override
	public PagedResponse<ContentSimpleResponse> getChildren(UUID id, Pageable pageable) {
		Content content = getById(id);

		return PagedResponse.of(
			contentRepository.findByParent(content, pageable),
			ContentSimpleResponse::from
		);
	}

	@Override
	public Content getById(UUID id) {
		return contentRepository.findById(id)
			.orElseThrow(ContentNotFoundException::new);
	}

	private Page<Content> fetchContents(ContentType contentType, Set<Long> genreIds, Pageable pageable) {
		boolean hasContentType = contentType != null;
		boolean hasGenres = genreIds != null && !genreIds.isEmpty();

		if (hasContentType && hasGenres) {
			return contentRepository.findDistinctByContentTypeAndGenres_idIn(contentType, genreIds, pageable);
		}

		if (hasContentType) {
			return contentRepository.findByContentType(contentType, pageable);
		}

		if (hasGenres) {
			return contentRepository.findDistinctByGenres_idIn(genreIds, pageable);
		}

		return contentRepository.findAll(pageable);
	}

}
