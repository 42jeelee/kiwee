package kr.co.jeelee.kiwee.domain.reward.entity;

import java.time.Duration;
import java.util.UUID;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.reward.model.RewardType;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import kr.co.jeelee.kiwee.global.vo.RewardCondition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rewards")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reward extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conferrer_id")
	private Member conferrer;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "domainType", column = @Column(name = "cond_domain_type", nullable = false)),
		@AttributeOverride(name = "domainId", column = @Column(name = "cond_domain_id")),
		@AttributeOverride(name = "activityType", column = @Column(name = "cond_activity_type", nullable = false)),
		@AttributeOverride(name = "activityCount", column = @Column(name = "cond_activity_count", nullable = false)),
		@AttributeOverride(name = "repeatPolicy", column = @Column(name = "cond_repeat_policy", nullable = false)),
		@AttributeOverride(name = "duration", column = @Column(name = "cond_duration")),
		@AttributeOverride(name = "consecutiveCount", column = @Column(name = "cond_consecutive_count", nullable = false)),
		@AttributeOverride(name = "matchPolicy", column = @Column(name = "cond_match_policy", nullable = false)),
		@AttributeOverride(name = "contentType", column = @Column(name = "cond_content_type"))
	})
	private RewardCondition condition;

	@Column(nullable = false)
	private RewardType rewardType;

	@Column
	private UUID rewardId;

	@Column
	private Duration duration;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private Integer exp;

	@Column(nullable = false)
	private Boolean isPublic;

	private Reward(
		Member conferrer, RewardCondition condition, RewardType rewardType, UUID rewardId,
		Duration duration, String title, String description, Integer exp, Boolean isPublic
	) {
		this.conferrer = conferrer;
		this.condition = condition;
		this.rewardType = rewardType;
		this.rewardId = rewardId;
		this.duration = duration;
		this.title = title;
		this.description = description;
		this.exp = exp;
		this.isPublic = isPublic;
	}

	public static Reward of(
		Member conferrer, RewardCondition condition, RewardType rewardType, UUID rewardId,
		Duration duration, String title, String description, Integer exp, Boolean isPublic
	) {
		return new Reward(
			conferrer, condition, rewardType, rewardId, duration, title, description, exp, isPublic
		);
	}

}
