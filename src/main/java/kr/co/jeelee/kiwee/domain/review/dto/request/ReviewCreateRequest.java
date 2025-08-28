package kr.co.jeelee.kiwee.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateRequest(
	@NotBlank(message = "message can't be Blank.") String message,
	@Min(value = -10, message = "star must be -10 or greater.")
	@Max(value = 10, message = "star must be 10 or less.")
	@NotNull(message = "star can't be Null.")
	Double star,
	@Min(value = 0, message = "consumedAmount must be 0 or greater.")
	Long consumedAmount, Boolean isSpoiler
) {
}
