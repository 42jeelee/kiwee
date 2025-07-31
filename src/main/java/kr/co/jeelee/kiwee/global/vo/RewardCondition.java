package kr.co.jeelee.kiwee.global.vo;

import java.time.Duration;

import jakarta.persistence.Embeddable;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.global.model.RewardMatchPolicy;
import kr.co.jeelee.kiwee.global.model.RewardRepeatPolicy;

@Embeddable
public record RewardCondition(
	ActivityCriterion criterion,
	RewardRepeatPolicy repeatPolicy,
	TimeRange timeRange,
	Duration duration,
	int consecutiveCount,
	RewardMatchPolicy matchPolicy,
	ContentType contentType
) {
}
