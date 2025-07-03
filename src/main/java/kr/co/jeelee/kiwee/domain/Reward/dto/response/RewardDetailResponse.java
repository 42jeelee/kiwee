package kr.co.jeelee.kiwee.domain.Reward.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.Reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.Reward.model.TriggerType;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;

public record RewardDetailResponse(
	UUID id, MemberSimpleResponse conferrer, Object source, Object reward,
	TriggerType triggerType, Integer triggerCount, String title, String description,
	Integer exp, Boolean isPublic, LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static RewardDetailResponse from(Reward reward, Object source, Object rewardObj) {
		return new RewardDetailResponse(
			reward.getId(),
			MemberSimpleResponse.from(reward.getConferrer()),
			source,
			rewardObj,
			reward.getTriggerType(),
			reward.getTriggerCount(),
			reward.getTitle(),
			reward.getDescription(),
			reward.getExp(),
			reward.getIsPublic(),
			reward.getUpdatedAt(),
			reward.getCreatedAt()
		);
	}
}
