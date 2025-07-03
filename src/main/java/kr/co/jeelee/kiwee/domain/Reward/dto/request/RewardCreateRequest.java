package kr.co.jeelee.kiwee.domain.Reward.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.domain.Reward.model.RewardType;
import kr.co.jeelee.kiwee.domain.Reward.model.TriggerType;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;

public record RewardCreateRequest(
	@NotNull(message = "sourceType can't be Null.") DomainType sourceType,
	@NotNull(message = "sourceId can't be Null.") UUID sourceId,
	@NotNull(message = "rewardType can't be Null.") RewardType rewardType,
	UUID rewardId,
	@NotNull(message = "triggerType can't be Null.") TriggerType triggerType,
	@NotNull(message = "triggerCount can't be Null.") Integer triggerCount,
	@NotBlank(message = "title can't be Blank.") String title,
	@NotBlank(message = "description can't be Blank.") String description,
	@NotNull(message = "exp can't be Null.") Integer exp,
	@NotNull(message = "isPublic can't be Null.") Boolean isPublic
) {
}
