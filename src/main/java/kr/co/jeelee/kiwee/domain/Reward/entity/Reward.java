package kr.co.jeelee.kiwee.domain.Reward.entity;

import java.time.Duration;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.Reward.model.RewardType;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
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

	@Column(nullable = false)
	private DomainType sourceType;

	@Column
	private UUID sourceId;

	@Column(nullable = false)
	private RewardType rewardType;

	@Column
	private UUID rewardId;

	@Column(nullable = false)
	private ActivityType activityType;

	@Column(nullable = false)
	private Integer activityCount;

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
		Member conferrer, DomainType sourceType, UUID sourceId,
		RewardType rewardType, UUID rewardId, ActivityType activityType,
		Integer activityCount, Duration duration, String title, String description,
		Integer exp, Boolean isPublic
	) {
		this.conferrer = conferrer;
		this.sourceType = sourceType;
		this.sourceId = sourceId;
		this.rewardType = rewardType;
		this.rewardId = rewardId;
		this.activityType = activityType;
		this.activityCount = activityCount;
		this.duration = duration;
		this.title = title;
		this.description = description;
		this.exp = exp;
		this.isPublic = isPublic;
	}

	public static Reward of(
		Member conferrer, DomainType sourceType, UUID sourceId,
		RewardType rewardType, UUID rewardId, ActivityType activityType,
		Integer activityCount, Duration duration, String title, String description,
		Integer exp, Boolean isPublic
	) {
		return new Reward(
			conferrer, sourceType, sourceId, rewardType, rewardId,
			activityType, activityCount, duration, title, description, exp, isPublic
		);
	}

}
