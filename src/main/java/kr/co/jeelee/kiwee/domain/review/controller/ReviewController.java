package kr.co.jeelee.kiwee.domain.review.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.review.dto.request.ReviewCreateRequest;
import kr.co.jeelee.kiwee.domain.review.dto.request.ReviewUpdateRequest;
import kr.co.jeelee.kiwee.domain.review.dto.response.ReviewDetailResponse;
import kr.co.jeelee.kiwee.domain.review.dto.response.ReviewSimpleResponse;
import kr.co.jeelee.kiwee.domain.review.service.ReviewService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Validated
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping(value = "/contents/{contentId}/members/me/reviews")
	public ReviewDetailResponse reviewMe(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID contentId,
		@Valid @RequestBody ReviewCreateRequest request
	) {
		return reviewService.review(contentId, principal.member().getId(), request);
	}

	@PostMapping(value = "/contents/{contentId}/members/{memberId}/reviews")
	public ReviewDetailResponse review(
		@PathVariable UUID contentId,
		@PathVariable UUID memberId,
		@Valid @RequestBody ReviewCreateRequest request
	) {
		return reviewService.review(contentId, memberId, request);
	}

	@GetMapping(value = "/contents/{contentId}/members/{memberId}/reviews")
	public PagedResponse<ReviewDetailResponse> getReviewDetails(
		@PathVariable UUID contentId,
		@PathVariable UUID memberId,
		@PageableDefault Pageable pageable
	) {
		return reviewService.getReviewDetails(contentId, memberId, pageable);
	}

	@GetMapping(value = "/reviews")
	public PagedResponse<ReviewSimpleResponse> getReviews(
		@RequestParam(required = false) UUID contentId,
		@RequestParam(required = false) UUID memberId,
		@PageableDefault Pageable pageable
	) {
		return reviewService.getReviews(contentId, memberId, pageable);
	}

	@PatchMapping(value = "/reviews/{id}")
	public ReviewDetailResponse updateReview(
		@PathVariable UUID id,
		@Valid @RequestBody ReviewUpdateRequest request
	) {
		return reviewService.updateReview(id, request);
	}

	@DeleteMapping(value = "/reviews/{id}")
	public ResponseEntity<Void> deleteReview(
		@PathVariable UUID id
	) {
		reviewService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
