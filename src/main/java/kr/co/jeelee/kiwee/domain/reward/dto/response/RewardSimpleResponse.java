package kr.co.jeelee.kiwee.domain.reward.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.reward.model.RewardType;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.model.DomainType;

public record RewardSimpleResponse(
	UUID id, String title, DomainType sourceType, RewardType rewardType,
	ActivityType activityType, Integer activityCount, Integer exp,
	LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static RewardSimpleResponse from(Reward reward) {
		return new RewardSimpleResponse(
			reward.getId(),
			reward.getTitle(),
			reward.getCondition().getCriterion().getDomainType(),
			reward.getRewardType(),
			reward.getCondition().getCriterion().getActivityType(),
			reward.getCondition().getCriterion().getActivityCount(),
			reward.getExp(),
			reward.getUpdatedAt(),
			reward.getCreatedAt()
		);
	}
}
