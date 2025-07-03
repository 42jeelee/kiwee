package kr.co.jeelee.kiwee.domain.memberActivity.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.memberActivity.model.ActivityType;
import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_activities")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberActivity extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actor_id", nullable = false)
	private Member actor;

	@Column(nullable = false)
	private ActivityType type;

	@Column(nullable = false)
	private DomainType sourceType;

	@Column
	private UUID sourceId;

	@Column(nullable = false)
	private String description;

	@OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RewardMember> rewardMembers;

	private MemberActivity(
		Member actor, ActivityType type, DomainType sourceType, UUID sourceId,
		String description, List<RewardMember> rewardMembers
	) {
		this.actor = actor;
		this.type = type;
		this.sourceType = sourceType;
		this.sourceId = sourceId;
		this.description = description;
		this.rewardMembers = rewardMembers;
	}

	public static MemberActivity of(
		Member actor, ActivityType type, DomainType sourceType, UUID sourceId,
		String description, List<RewardMember> rewardMembers
	) {
		return new MemberActivity(actor, type, sourceType, sourceId, description, rewardMembers);
	}

	public void addRewardMembers(List<RewardMember> rewardMembers) {
		this.rewardMembers.addAll(rewardMembers);
	}

}
