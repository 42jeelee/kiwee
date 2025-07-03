package kr.co.jeelee.kiwee.domain.Reward.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.Reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.Reward.model.TriggerType;

public record RewardSimpleResponse(
	UUID id, String title, Object source, Object reward,
	TriggerType triggerType, Integer triggerCount, Integer exp,
	LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static RewardSimpleResponse from(Reward reward, Object source, Object rewardObj) {
		return new RewardSimpleResponse(
			reward.getId(),
			reward.getTitle(),
			source,
			rewardObj,
			reward.getTriggerType(),
			reward.getTriggerCount(),
			reward.getExp(),
			reward.getUpdatedAt(),
			reward.getCreatedAt()
		);
	}
}
