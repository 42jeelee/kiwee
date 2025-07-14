package kr.co.jeelee.kiwee.domain.Reward.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.Reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.Reward.model.RewardType;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import kr.co.jeelee.kiwee.global.resolver.DomainResponseResolver;

public record RewardDetailResponse(
	UUID id, MemberSimpleResponse conferrer, Object source, Object reward,
	ActivityType activityType, Integer activityCount, String title, String description,
	Integer exp, Boolean isPublic, LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static RewardDetailResponse from(Reward reward, DomainObjectResolver resolver) {
		return new RewardDetailResponse(
			reward.getId(),
			MemberSimpleResponse.from(reward.getConferrer()),
			DomainResponseResolver.toResponse(resolver.resolve(reward.getSourceType(), reward.getSourceId())),
			reward.getRewardType() != RewardType.NONE
				? DomainResponseResolver.toResponse(
					resolver.resolve(reward.getRewardType().getDomainType(), reward.getRewardId())
				) : null,
			reward.getActivityType(),
			reward.getActivityCount(),
			reward.getTitle(),
			reward.getDescription(),
			reward.getExp(),
			reward.getIsPublic(),
			reward.getUpdatedAt(),
			reward.getCreatedAt()
		);
	}
}
