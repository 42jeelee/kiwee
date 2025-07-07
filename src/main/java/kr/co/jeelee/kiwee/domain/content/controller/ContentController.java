package kr.co.jeelee.kiwee.domain.content.controller;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.content.dto.request.ContentCreateRequest;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentDetailResponse;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentSimpleResponse;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.domain.content.service.ContentService;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/contents")
@RequiredArgsConstructor
@Validated
public class ContentController {

	private final ContentService contentService;

	@PreAuthorize(value = "hasRole('CREATE_CONTENT')")
	@PostMapping
	public ContentDetailResponse createContent(
		@Valid @RequestBody ContentCreateRequest contentCreateRequest
	) {
		return contentService.createContent(contentCreateRequest);
	}

	@GetMapping(value = "/{id}")
	public ContentDetailResponse getContentById(
		@PathVariable UUID id
	) {
		return contentService.getContentDetail(id);
	}

	@GetMapping
	public PagedResponse<ContentSimpleResponse> getContents(
		@RequestParam ContentType contentType,
		@RequestParam Set<Long> genreIds,
		@PageableDefault Pageable pageable
	) {
		return contentService.getContents(contentType, genreIds, pageable);
	}

	@GetMapping(value = "/{id}/children")
	public PagedResponse<ContentSimpleResponse> getChildren(
		@PathVariable UUID id,
		@PageableDefault Pageable pageable
	) {
		return contentService.getChildren(id, pageable);
	}

}
