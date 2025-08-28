package kr.co.jeelee.kiwee.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ReviewUpdateRequest(
	String message,
	@Min(value = -10, message = "star must be -10 or greater.")
	@Max(value = 10, message = "star must be 10 or less.")
	Double star,
	@Min(value = 0, message = "consumedAmount must be 0 or greater.")
	Long consumedAmount,
	Boolean isSpoiler
) {
}
