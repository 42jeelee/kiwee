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
			reward.getCondition().criterion().domainType(),
			reward.getRewardType(),
			reward.getCondition().criterion().activityType(),
			reward.getCondition().criterion().activityCount(),
			reward.getExp(),
			reward.getUpdatedAt(),
			reward.getCreatedAt()
		);
	}
}
