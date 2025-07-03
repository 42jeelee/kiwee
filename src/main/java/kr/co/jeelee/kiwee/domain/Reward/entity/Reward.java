package kr.co.jeelee.kiwee.domain.Reward.entity;

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
import kr.co.jeelee.kiwee.domain.Reward.model.TriggerType;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
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

	@Column(nullable = false)
	private UUID sourceId;

	@Column(nullable = false)
	private RewardType rewardType;

	@Column
	private UUID rewardId;

	@Column(nullable = false)
	private TriggerType triggerType;

	@Column(nullable = false)
	private Integer triggerCount;

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
		RewardType rewardType, UUID rewardId, TriggerType triggerType,
		Integer triggerCount, String title, String description,
		Integer exp, Boolean isPublic
	) {
		this.conferrer = conferrer;
		this.sourceType = sourceType;
		this.sourceId = sourceId;
		this.rewardType = rewardType;
		this.rewardId = rewardId;
		this.triggerType = triggerType;
		this.triggerCount = triggerCount;
		this.title = title;
		this.description = description;
		this.exp = exp;
		this.isPublic = isPublic;
	}

	public static Reward of(
		Member conferrer, DomainType sourceType, UUID sourceId,
		RewardType rewardType, UUID rewardId, TriggerType triggerType,
		Integer triggerCount, String title, String description,
		Integer exp, Boolean isPublic
	) {
		return new Reward(
			conferrer, sourceType, sourceId, rewardType, rewardId,
			triggerType, triggerCount, title, description, exp, isPublic
		);
	}

}
