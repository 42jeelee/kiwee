package kr.co.jeelee.kiwee.domain.content.controller;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.content.dto.request.ContentCreateRequest;
import kr.co.jeelee.kiwee.domain.content.dto.request.ContentCreateWithPlatformRequest;
import kr.co.jeelee.kiwee.domain.content.dto.request.ContentUpdateRequest;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentDetailResponse;
import kr.co.jeelee.kiwee.global.dto.response.OnlyIdResponse;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentSimpleResponse;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.domain.content.service.ContentService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Validated
public class ContentController {

	private final ContentService contentService;

	@PreAuthorize(value = "hasRole('CREATE_CONTENT')")
	@PostMapping(value = "/contents")
	public ContentDetailResponse createContent(
		@Valid @RequestBody ContentCreateRequest contentCreateRequest
	) {
		return contentService.createContent(contentCreateRequest);
	}

	@PostMapping(value = "/platforms/{platformId}/contents")
	public ContentDetailResponse createContent(
		@PathVariable UUID platformId,
		@Valid @RequestBody ContentCreateWithPlatformRequest request
	) {
		return contentService.createContent(platformId, request);
	}

	@GetMapping(value = "/platforms/{platformId}/contents/{idInPlatform}")
	public OnlyIdResponse getContentByPlatform(
		@PathVariable UUID platformId,
		@PathVariable String idInPlatform
	) {
		return OnlyIdResponse.from(contentService.getContentIdByPlatform(platformId, idInPlatform));
	}

	@GetMapping(value = "/contents/{id}")
	public ContentDetailResponse getContentById(
		@PathVariable UUID id
	) {
		return contentService.getContentDetail(id);
	}

	@GetMapping(value = "/contents")
	public PagedResponse<ContentSimpleResponse> getContents(
		@RequestParam(required = false) ContentType contentType,
		@RequestParam(required = false) Set<Long> genreIds,
		@PageableDefault Pageable pageable
	) {
		return contentService.getContents(contentType, genreIds, pageable);
	}

	@GetMapping(value = "/contents/{contentId}/children")
	public PagedResponse<ContentSimpleResponse> getContentsBySeriesId(
		@PathVariable UUID contentId,
		@PageableDefault Pageable pageable
	) {
		return contentService.getContentsByParentId(contentId, pageable);
	}

	@PreAuthorize(value = "hasRole('EDIT_CONTENT')")
	@PatchMapping(value = "/contents/{id}")
	public ContentDetailResponse updateContent(
		@PathVariable UUID id,
		@Valid @RequestBody ContentUpdateRequest contentUpdateRequest
	) {
		return contentService.updateContent(id, contentUpdateRequest);
	}

	@PreAuthorize(value = "hasRole('DELETE_CONTENT')")
	@DeleteMapping(value = "/contents/{id}")
	public ResponseEntity<Void> deleteContent(
		@PathVariable UUID id
	) {
		contentService.deleteContent(id);
		return ResponseEntity.noContent().build();
	}

}
