package kr.co.jeelee.kiwee.global.vo;

import java.time.Duration;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.global.model.RewardMatchPolicy;
import kr.co.jeelee.kiwee.global.model.RewardRepeatPolicy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class RewardCondition {

	@Embedded
	private ActivityCriterion criterion;

	@Column(name = "cond_repeat_policy")
	private RewardRepeatPolicy repeatPolicy;

	@Embedded
	private TimeRange timeRange;

	@Column(name = "cond_duration")
	private Duration duration;

	@Column(name = "cond_consecutive_count", nullable = false)
	private int consecutiveCount;

	@Column(name = "cond_match_policy", nullable = false)
	private RewardMatchPolicy matchPolicy;

	@Column(name = "cond_content_type")
	private ContentType contentType;

}
