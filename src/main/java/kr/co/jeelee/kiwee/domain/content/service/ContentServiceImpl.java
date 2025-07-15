package kr.co.jeelee.kiwee.domain.content.service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.content.dto.request.ContentCreateRequest;
import kr.co.jeelee.kiwee.domain.content.dto.request.ContentUpdateRequest;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentDetailResponse;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentSimpleResponse;
import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.content.exception.ContentNotFoundException;
import kr.co.jeelee.kiwee.domain.content.model.ContentLevel;
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
			platforms,
			request.applicationId()
		);

		return ContentDetailResponse.from(contentRepository.save(content));
	}

	@Override
	public ContentDetailResponse getContentDetail(UUID id) {
		return ContentDetailResponse.from(getById(id));
	}

	@Override
	@Transactional
	public ContentDetailResponse updateContent(UUID id, ContentUpdateRequest request) {
		Content content = getById(id);

		try {
			Content parent = request.parent() != null
				? getById(request.parent())
				: null;

			Set<Platform> platforms = request.platformIds().stream()
				.map(platformService::getById)
				.collect(Collectors.toSet());

			Set<Genre> genres = request.genres().stream()
				.map(genreService::getOrCreateGenre)
				.collect(Collectors.toSet());

			updateTitleIfChanged(content, request.title());
			updateDescriptionIfChanged(content, request.description());
			updateRatingIfChanged(content, request.rating());
			updateImageUrlIfChanged(content, request.imageUrl());
			updateHomePageIfChanged(content, request.homePage());
			updateContentTypeAndLevelIfChanged(content, request.contentType(), request.contentLevel());
			content.updateGenres(genres);
			content.updatePlatforms(platforms);
			updateParentIfChanged(content, parent);
			updateApplicationIdIfChanged(content, request.applicationId());

			return ContentDetailResponse.from(content);
		} catch (ContentNotFoundException e) {
			throw new FieldValidationException("parentId", "존재하지 않는 콘텐츠입니다.");
		}
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
	public Content getByApplicationId(String applicationId) {
		return contentRepository.findByApplicationId(applicationId)
			.orElse(null);
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

	private void updateTitleIfChanged(Content content, String title) {
		if (title != null && !title.equals(content.getTitle())) {
			content.updateTitle(title);
		}
	}

	private void updateDescriptionIfChanged(Content content, String description) {
		if (description != null && !description.equals(content.getDescription())) {
			content.updateDescription(description);
		}
	}

	private void updateRatingIfChanged(Content content, Double rating) {
		if (rating != null && rating.equals(content.getRating())) {
			content.updateRating(rating);
		}
	}

	private void updateImageUrlIfChanged(Content content, String imageUrl) {
		if (imageUrl != null && !imageUrl.equals(content.getImageUrl())) {
			content.updateImageUrl(imageUrl);
		}
	}

	private void updateHomePageIfChanged(Content content, String homePage) {
		if (homePage != null && !homePage.equals(content.getHomePage())) {
			content.updateHomePage(homePage);
		}
	}

	private void updateContentTypeAndLevelIfChanged(Content content, ContentType type, ContentLevel level) {
		if (
			(type != null && type != content.getContentType())
			|| (level != null && level != content.getContentLevel())
		) {
			content.updateContentTypeAndLevel(type, level);
		}
	}

	private void updateParentIfChanged(Content content, Content parent) {
		if (parent != null && !content.getParent().equals(parent)) {
			content.updateParent(parent);
		}
	}

	private void updateApplicationIdIfChanged(Content content, String applicationId) {
		if (applicationId != null && !content.getApplicationId().equals(applicationId)) {
			content.updateApplicationId(applicationId);
		}
	}

}
