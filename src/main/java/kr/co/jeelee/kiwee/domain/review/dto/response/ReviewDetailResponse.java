package kr.co.jeelee.kiwee.domain.review.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.review.entity.Review;

public record ReviewDetailResponse(
	UUID id, ContentMemberSimpleResponse info, String message, Double star,
	Integer completedCount, Long consumedAmount, Boolean isSpoiler,
	LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static ReviewDetailResponse from(Review review) {
		return new ReviewDetailResponse(
			review.getId(),
			ContentMemberSimpleResponse.from(review.getContentMember()),
			review.getMessage(),
			review.getStar(),
			review.getCompletedCount(),
			review.getConsumedAmount(),
			review.getIsSpoiler(),
			review.getUpdatedAt(),
			review.getCreatedAt()
		);
	}
}
