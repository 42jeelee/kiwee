package kr.co.jeelee.kiwee.domain.Reward.event;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.Reward.model.TriggerType;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;

public record RewardEvent(
	UUID awardeeId,
	DomainType sourceType, UUID sourceId,
	TriggerType triggerType, Integer triggerCount,
	UUID activityId
) {
	public static RewardEvent of(
		UUID awardeeId,
		DomainType sourceType, UUID sourceId,
		TriggerType triggerType, Integer triggerCount,
		UUID activityId
	) {
		return new RewardEvent(
			awardeeId,
			sourceType,
			sourceId,
			triggerType,
			triggerCount,
			activityId
		);
	}
}
