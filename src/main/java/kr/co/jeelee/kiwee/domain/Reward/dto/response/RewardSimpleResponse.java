package kr.co.jeelee.kiwee.domain.Reward.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.Reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.Reward.model.RewardType;
import kr.co.jeelee.kiwee.domain.memberActivity.model.ActivityType;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import kr.co.jeelee.kiwee.global.resolver.DomainResponseResolver;

public record RewardSimpleResponse(
	UUID id, String title, Object source, Object reward,
	ActivityType activityType, Integer activityCount, Integer exp,
	LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static RewardSimpleResponse from(Reward reward, DomainObjectResolver resolver) {
		return new RewardSimpleResponse(
			reward.getId(),
			reward.getTitle(),
			DomainResponseResolver.toResponse(resolver.resolve(reward.getSourceType(), reward.getSourceId())),
			reward.getRewardType() != RewardType.NONE
				? DomainResponseResolver.toResponse(
					resolver.resolve(reward.getRewardType().getDomainType(), reward.getRewardId())
				) : null,
			reward.getActivityType(),
			reward.getActivityCount(),
			reward.getExp(),
			reward.getUpdatedAt(),
			reward.getCreatedAt()
		);
	}
}
