package kr.co.jeelee.kiwee.domain.Reward.event;

import java.util.UUID;

public record RewardEvent(
	UUID activityId
) {
	public static RewardEvent of(UUID activityId) {
		return new RewardEvent(activityId);
	}
}
