package kr.co.jeelee.kiwee.domain.Reward.dto.request;

import java.time.Duration;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.domain.Reward.model.RewardType;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.model.ActivityType;

public record RewardCreateRequest(
	@NotNull(message = "sourceType can't be Null.") DomainType sourceType,
	UUID sourceId,
	@NotNull(message = "rewardType can't be Null.") RewardType rewardType,
	UUID rewardId,
	@NotNull(message = "activityType can't be Null.") ActivityType activityType,
	@NotNull(message = "activityCount can't be Null.") Integer activityCount,
	Duration duration,
	@NotBlank(message = "title can't be Blank.") String title,
	@NotBlank(message = "description can't be Blank.") String description,
	Integer exp,
	@NotNull(message = "isPublic can't be Null.") Boolean isPublic
) {
}
