package kr.co.jeelee.kiwee.domain.reward.dto.request;

import java.time.Duration;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.domain.reward.model.RewardType;

public record RewardCreateRequest(
	@Valid @NotNull(message = "condition can't be Null.") RewardConditionRequest condition,
	@NotNull(message = "rewardType can't be Null.") RewardType rewardType,
	UUID rewardId,
	Duration duration,
	@NotBlank(message = "title can't be Blank.") String title,
	@NotBlank(message = "description can't be Blank.") String description,
	@NotBlank(message = "successMessage can't be Blank.") String successMessage,
	Integer exp,
	@NotNull(message = "isPublic can't be Null.") Boolean isPublic
) {
}
