package kr.co.jeelee.kiwee.domain.content.service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.content.dto.request.ContentCreateRequest;
import kr.co.jeelee.kiwee.domain.content.dto.request.ContentCreateWithPlatformRequest;
import kr.co.jeelee.kiwee.domain.content.dto.request.ContentUpdateRequest;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentDetailResponse;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentSimpleResponse;
import kr.co.jeelee.kiwee.domain.content.dto.response.PlatformContentResponse;
import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.content.entity.PlatformContent;
import kr.co.jeelee.kiwee.domain.content.exception.ContentNotFoundException;
import kr.co.jeelee.kiwee.domain.content.exception.PlatformContentNotFoundException;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.domain.content.repository.ContentRepository;
import kr.co.jeelee.kiwee.domain.content.repository.PlatformContentRepository;
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
	private final PlatformContentRepository platformContentRepository;

	private final PlatformService platformService;
	private final GenreService genreService;

	@Override
	@Transactional
	public ContentDetailResponse createContent(ContentCreateRequest request) {
		return ContentDetailResponse.from(contentRepository.save(requestToContent(request)));
	}

	@Override
	@Transactional
	public ContentDetailResponse createContent(UUID platformId, ContentCreateWithPlatformRequest request) {
		Platform platform = platformService.getById(platformId);

		return createWithPlatform(platform, request);
	}

	@Override
	@Transactional
	public ContentDetailResponse createContent(String platformProvider, ContentCreateWithPlatformRequest request) {
		Platform platform = platformService.getEntityByProvider(platformProvider);

		return createWithPlatform(platform, request);
	}

	@Override
	public ContentDetailResponse getContentDetail(UUID id) {
		return ContentDetailResponse.from(getById(id));
	}

	@Override
	public PagedResponse<PlatformContentResponse> getPlatformsByContentId(UUID contentId, Pageable pageable) {
		return PagedResponse.of(
			platformContentRepository.findByContent_Id(contentId, pageable),
			PlatformContentResponse::from
		);
	}

	@Override
	@Transactional
	public ContentDetailResponse updateContent(UUID id, ContentUpdateRequest request) {
		Content content = getById(id);

		updateTitleIfChanged(content, request.title());
		updateOverviewIfChanged(content, request.overview());
		updateRatingIfChanged(content, request.rating());
		updateImageUrlIfChanged(content, request.imageUrl());
		updateHomepageIfChanged(content, request.homepage());
		updateContentTypeIfChanged(content, request.contentType());
		updateParentIfChanged(content, request.parentId());
		updateGenresIfChanged(content, request.genreIds());

		return ContentDetailResponse.from(content);
	}

	@Override
	public void deleteContent(UUID id) {
		Content content = getById(id);
		contentRepository.delete(content);
	}

	@Override
	public PagedResponse<ContentSimpleResponse> getContents(Set<ContentType> contentTypes, Set<Long> genreIds,
		Pageable pageable) {
		return PagedResponse.of(
			fetchContents(contentTypes, genreIds, pageable),
			ContentSimpleResponse::from
		);
	}

	@Override
	public PagedResponse<ContentSimpleResponse> getContentsByParentId(UUID parentId, Pageable pageable) {
		Content parent = getById(parentId);

		return PagedResponse.of(
			contentRepository.findByParent(parent, pageable),
			ContentSimpleResponse::from
		);
	}

	@Override
	public Content getById(UUID id) {
		return contentRepository.findById(id)
			.orElseThrow(ContentNotFoundException::new);
	}

	@Override
	public Content getRootById(UUID id) {
		return contentRepository.findRootByChildrenId(id)
			.orElseThrow(ContentNotFoundException::new);
	}

	@Override
	public Content getByPlatform(UUID platformId, String idInPlatform) {
		return platformContentRepository.findByPlatformIdAndIdInPlatform(platformId, idInPlatform)
			.map(PlatformContent::getContent)
			.orElseThrow(ContentNotFoundException::new);
	}

	@Override
	public PlatformContentResponse getPlatformContentByContentId(String platformProvider, UUID contentId) {
		return platformContentRepository.findByPlatform_ProviderAndContent_Id(platformProvider, contentId)
			.map(PlatformContentResponse::from)
			.orElseThrow(PlatformContentNotFoundException::new);
	}

	@Override
	public PlatformContentResponse getPlatformContentByContentId(UUID platformId, UUID contentId) {
		return platformContentRepository.findByPlatform_IdAndContent_Id(platformId, contentId)
			.map(PlatformContentResponse::from)
			.orElseThrow(PlatformContentNotFoundException::new);
	}

	@Override
	public UUID getContentIdByPlatform(UUID platformId, String idInPlatform) {
		return getByPlatform(platformId, idInPlatform).getId();
	}

	@Override
	public UUID getContentIdByPlatformProvider(String platformProvider, String idInPlatform) {
		return platformContentRepository.findByPlatform_ProviderAndIdInPlatform(platformProvider, idInPlatform)
			.map(PlatformContent::getContent)
			.map(Content::getId)
			.orElseThrow(ContentNotFoundException::new);
	}

	private Content requestToContent(ContentCreateRequest request) {
		Content parent = request.parentId() != null
			? getParent(request.parentId())
			: null;

		if (parent != null && request.childrenIdx() == null) {
			throw new FieldValidationException("childrenIdx", "부모가 있을 경우 인덱스가 필요합니다.");
		}

		Set<Genre> genres = request.genres() != null
			? request.genres().stream()
			.map(genreService::getById)
			.collect(Collectors.toSet())
			: null;

		return Content.of(
			request.title(),
			request.overview(),
			request.rating(),
			request.imageUrl(),
			request.homepage(),
			request.totalAmount(),
			request.contentType(),
			parent,
			request.childrenIdx(),
			genres
		);
	}

	private ContentDetailResponse createWithPlatform(Platform platform, ContentCreateWithPlatformRequest request) {
		if (platformContentRepository.existsByPlatformIdAndIdInPlatform(platform.getId(), request.idInPlatform())) {
			throw new FieldValidationException("idInPlatform", "이미 존재하는 컨텐츠가 있습니다.");
		}

		Content content = requestToContent(request.content());

		Set<Genre> genres = request.genres() != null
			? request.genres().stream()
			.map(genreService::getOrCreateGenre)
			.collect(Collectors.toSet())
			: null;

		content.updateGenres(genres);

		Content savedContent = contentRepository.save(content);

		platformContentRepository.save(PlatformContent.of(
			platform,
			savedContent,
			request.idInPlatform()
		));

		return ContentDetailResponse.from(savedContent);
	}

	private Content getParent(UUID parentId) {
		try {
			return getById(parentId);
		} catch (ContentNotFoundException e) {
			throw new FieldValidationException("parentId", "해당 콘텐츠가 존재하지 않습니다.");
		}
	}

	private Page<Content> fetchContents(Set<ContentType> contentTypes, Set<Long> genreIds, Pageable pageable) {
		boolean hasContentTypes = contentTypes != null && !contentTypes.isEmpty();
		boolean hasGenres = genreIds != null && !genreIds.isEmpty();

		if (hasContentTypes && hasGenres) {
			return contentRepository.findDistinctByContentTypeInAndGenres_idIn(contentTypes, genreIds, pageable);
		}

		if (hasContentTypes) {
			return contentRepository.findByContentTypeIn(contentTypes, pageable);
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

	private void updateOverviewIfChanged(Content content, String overview) {
		if (overview != null && !overview.equals(content.getOverview())) {
			content.updateDescription(overview);
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

	private void updateHomepageIfChanged(Content content, String homePage) {
		if (homePage != null && !homePage.equals(content.getHomepage())) {
			content.updateHomepage(homePage);
		}
	}

	private void updateContentTypeIfChanged(Content content, ContentType contentType) {
		if (contentType != null && contentType.equals(content.getContentType())) {
			content.updateContentType(contentType);
		}
	}

	private void updateParentIfChanged(Content content, UUID parentId) {
		if (parentId != null) {
			Content parent = getParent(parentId);

			if (!content.getParent().getId().equals(parent.getId())) {
				content.updateParent(parent);
			}
		}
	}

	private void updateGenresIfChanged(Content content, Set<Long> genreIds) {
		if (genreIds != null && !genreIds.isEmpty()) {
			Set<Genre> genres = genreIds.stream()
				.map(genreService::getById)
				.collect(Collectors.toSet());

			content.updateGenres(genres);
		}
	}

}
