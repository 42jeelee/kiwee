package kr.co.jeelee.kiwee.domain.reward.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.reward.model.RewardType;
import kr.co.jeelee.kiwee.global.model.ActivityType;
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
			DomainResponseResolver.toResponse(resolver.resolve(
				reward.getCondition().criterion().domainType(),
				reward.getCondition().criterion().domainId()
			)),
			reward.getRewardType() != RewardType.NONE
				? DomainResponseResolver.toResponse(
					resolver.resolve(reward.getRewardType().getDomainType(), reward.getRewardId())
				) : null,
			reward.getCondition().criterion().activityType(),
			reward.getCondition().criterion().activityCount(),
			reward.getExp(),
			reward.getUpdatedAt(),
			reward.getCreatedAt()
		);
	}
}
