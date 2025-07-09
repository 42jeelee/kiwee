package kr.co.jeelee.kiwee.domain.Reward.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.Reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.memberActivity.model.ActivityType;

public record RewardDetailResponse(
	UUID id, MemberSimpleResponse conferrer, Object source, Object reward,
	ActivityType activityType, Integer activityCount, String title, String description,
	Integer exp, Boolean isPublic, LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static RewardDetailResponse from(Reward reward, Object source, Object rewardObj) {
		return new RewardDetailResponse(
			reward.getId(),
			MemberSimpleResponse.from(reward.getConferrer()),
			source,
			rewardObj,
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
