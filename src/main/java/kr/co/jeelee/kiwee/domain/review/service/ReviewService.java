package kr.co.jeelee.kiwee.domain.review.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.review.dto.request.ReviewCreateRequest;
import kr.co.jeelee.kiwee.domain.review.dto.request.ReviewUpdateRequest;
import kr.co.jeelee.kiwee.domain.review.dto.response.ReviewDetailResponse;
import kr.co.jeelee.kiwee.domain.review.dto.response.ReviewSimpleResponse;
import kr.co.jeelee.kiwee.domain.review.entity.Review;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface ReviewService {

	ReviewDetailResponse review(UUID contentId, UUID memberId, ReviewCreateRequest request);

	PagedResponse<ReviewDetailResponse> getReviewDetails(UUID contentId, UUID memberId, Pageable pageable);

	PagedResponse<ReviewDetailResponse> getReviews(UUID contentId, UUID memberId, Pageable pageable);

	PagedResponse<ReviewSimpleResponse> getReviewsByConsumedAmount(UUID contentId, Long consumedAmount, Pageable pageable);

	ReviewDetailResponse updateReview(UUID id, ReviewUpdateRequest request);

	List<Long> getReviewConsumedAmountByContentId(UUID contentId);

	void deleteById(UUID id);

	Review getById(UUID id);

}
