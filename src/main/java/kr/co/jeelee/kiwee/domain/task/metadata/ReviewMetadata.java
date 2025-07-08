package kr.co.jeelee.kiwee.domain.task.metadata;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewMetadata(
	@NotNull(message = "contentId can't be Null.") UUID contentId,
	@Min(value = 0, message = "star cannot be less than 0.")
	@Max(value = 5, message = "star cannot be greater than 5.")
	@NotNull(message = "star can't be Null.")
	Double star,
	@NotBlank(message = "message can't be Blank.") String message
) {
}
