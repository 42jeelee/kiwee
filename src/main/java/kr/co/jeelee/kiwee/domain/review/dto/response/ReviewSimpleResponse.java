package kr.co.jeelee.kiwee.domain.review.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.review.entity.Review;

public record ReviewSimpleResponse(
	UUID id, UUID memberId, UUID contentId, String message, Double star, Long consumedAmount,
	LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static ReviewSimpleResponse from(Review review) {
		return new ReviewSimpleResponse(
			review.getId(),
			review.getContentMember().getMember().getId(),
			review.getContentMember().getContent().getId(),
			review.getMessage(),
			review.getStar(),
			review.getConsumedAmount(),
			review.getUpdatedAt(),
			review.getCreatedAt()
		);
	}
}
