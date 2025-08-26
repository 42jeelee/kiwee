package kr.co.jeelee.kiwee.domain.contentMember.controller;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.domain.contentMember.dto.request.ContentMemberCreateRequest;
import kr.co.jeelee.kiwee.domain.contentMember.dto.request.ContentMemberUpdateRequest;
import kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberCompletedRateResponse;
import kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberStarResponse;
import kr.co.jeelee.kiwee.domain.contentMember.service.ContentMemberService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Validated
public class ContentMemberController {

	private final ContentMemberService contentMemberService;

	@PostMapping(value = "/contents/{contentId}/members/{memberId}")
	public ContentMemberDetailResponse createContentMember(
		@PathVariable UUID contentId,
		@PathVariable UUID memberId,
		@Valid @RequestBody ContentMemberCreateRequest request
	) {
		return contentMemberService.createContentMember(contentId, memberId, request);
	}

	@GetMapping(value = "/contents/{contentId}/members/{memberId}")
	public ContentMemberDetailResponse getContentMember(
		@PathVariable UUID contentId,
		@PathVariable UUID memberId
	) {
		return contentMemberService.getContentMember(contentId, memberId);
	}

	@GetMapping(value = "/contents/{contentId}/members")
	public PagedResponse<ContentMemberSimpleResponse> getContentMembersByContentId(
		@PathVariable UUID contentId,
		@PageableDefault Pageable pageable
	) {
		return contentMemberService.getContentMembersByContentId(contentId, pageable);
	}

	@GetMapping(value = "/members/{memberId}/contents")
	public PagedResponse<ContentMemberSimpleResponse> getContentMembersByMemberId(
		@PathVariable UUID memberId,
		@RequestParam(name = "contentType", required = false) Set<ContentType> contentTypes,
		@PageableDefault Pageable pageable
	) {
		return contentMemberService.getContentMembersByMemberId(memberId, contentTypes, pageable);
	}

	@GetMapping(value = "/contents/{contentId}/stars")
	public ContentMemberStarResponse getAverageStar(
		@PathVariable UUID contentId
	) {
		return contentMemberService.getAverageStar(contentId);
	}

	@GetMapping(value = "/contents/{contentId}/members/{memberId}/completedRate")
	public ContentMemberCompletedRateResponse getCompletedRate(
		@PathVariable UUID contentId,
		@PathVariable UUID memberId
	) {
		return ContentMemberCompletedRateResponse.from(contentMemberService.getCompletedRate(contentId, memberId));
	}

	@PatchMapping(value = "/contents/{contentId}/members/{memberId}")
	public ContentMemberDetailResponse updateContentMember(
		@PathVariable UUID contentId,
		@PathVariable UUID memberId,
		@Valid @RequestBody ContentMemberUpdateRequest request
	) {
		return contentMemberService.updateContentMember(contentId, memberId, request);
	}

	@DeleteMapping(value = "/contents/{contentId}/members/{memberId}")
	public ResponseEntity<Void> deleteContentMember(
		@PathVariable UUID contentId,
		@PathVariable UUID memberId
	) {
		contentMemberService.deleteContentMember(contentId, memberId);
		return ResponseEntity.noContent().build();
	}

}
