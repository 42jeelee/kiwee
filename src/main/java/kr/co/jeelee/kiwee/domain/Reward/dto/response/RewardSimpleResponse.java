package kr.co.jeelee.kiwee.domain.Reward.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.Reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.memberActivity.model.ActivityType;

public record RewardSimpleResponse(
	UUID id, String title, Object source, Object reward,
	ActivityType activityType, Integer activityCount, Integer exp,
	LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static RewardSimpleResponse from(Reward reward, Object source, Object rewardObj) {
		return new RewardSimpleResponse(
			reward.getId(),
			reward.getTitle(),
			source,
			rewardObj,
			reward.getActivityType(),
			reward.getActivityCount(),
			reward.getExp(),
			reward.getUpdatedAt(),
			reward.getCreatedAt()
		);
	}
}
