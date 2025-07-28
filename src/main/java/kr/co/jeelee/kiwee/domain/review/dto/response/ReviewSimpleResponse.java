package kr.co.jeelee.kiwee.domain.review.dto.response;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.review.entity.Review;

public record ReviewSimpleResponse(
	UUID id, String message, Double star, Long consumedAmount
) {
	public static ReviewSimpleResponse from(Review review) {
		return new ReviewSimpleResponse(
			review.getId(),
			review.getMessage(),
			review.getStar(),
			review.getConsumedAmount()
		);
	}
}
