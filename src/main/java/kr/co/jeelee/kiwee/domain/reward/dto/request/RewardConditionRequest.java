package kr.co.jeelee.kiwee.domain.reward.dto.request;

import java.time.Duration;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.model.RewardMatchPolicy;
import kr.co.jeelee.kiwee.global.model.RewardRepeatPolicy;
import kr.co.jeelee.kiwee.global.vo.ActivityCriterion;
import kr.co.jeelee.kiwee.global.vo.RewardCondition;
import kr.co.jeelee.kiwee.global.vo.TimeRange;

public record RewardConditionRequest(
	@NotNull(message = "sourceType can't be Null.") DomainType sourceType,
	UUID sourceId,
	@NotNull(message = "activityType can't be Null.") ActivityType activityType,
	@Min(value = 0, message = "activityCount must be 0 or greater.") int activityCount,
	@NotNull(message = "repeatPolicy can't be Null.") RewardRepeatPolicy repeatPolicy,
	TimeRange timeRange,
	Duration duration,
	@Min(value = 0, message = "consecutiveCount must be 0 or greater.") int consecutiveCount,
	@NotNull(message = "matchPolicy can't be Null.") RewardMatchPolicy matchPolicy,
	ContentType contentType
) {
	public RewardCondition toRewardCondition() {
		return new RewardCondition(
			ActivityCriterion.of(sourceType, sourceId, activityType, activityCount),
			repeatPolicy, timeRange, duration, consecutiveCount, matchPolicy, contentType
		);
	}
}
