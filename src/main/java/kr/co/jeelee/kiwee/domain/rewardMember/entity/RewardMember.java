package kr.co.jeelee.kiwee.domain.rewardMember.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.Reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reward_members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardMember extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "awardee_id")
	private Member awardee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reward_id")
	private Reward reward;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activity_id")
	private MemberActivity activity;

	private RewardMember(Member awardee, Reward reward, MemberActivity activity) {
		this.awardee = awardee;
		this.reward = reward;
		this.activity = activity;
	}

	public static RewardMember of(Member awardee, Reward reward, MemberActivity activity) {
		return new RewardMember(awardee, reward, activity);
	}

}
