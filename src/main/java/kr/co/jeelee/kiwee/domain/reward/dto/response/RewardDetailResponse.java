package kr.co.jeelee.kiwee.domain.reward.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.reward.model.RewardType;
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
			reward.getTitle(),
			reward.getDescription(),
			reward.getExp(),
			reward.getIsPublic(),
			reward.getUpdatedAt(),
			reward.getCreatedAt()
		);
	}
}
