package kr.co.jeelee.kiwee.domain.questMember.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberVerificationStatus;
import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "quest_member_verifications",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_quest_member_verification", columnNames = {"quest_member_id", "verified_date"})
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestMemberVerification extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quest_member_id")
	private QuestMember questMember;

	@Column(nullable = false)
	private LocalDate verifiedDate;

	@Column(nullable = false)
	private QuestMemberVerificationStatus status;

	@Column
	private String contentType;

	@Column
	private String contentUrl;

	@Column
	private String message;

	@Column(nullable = false)
	private LocalDateTime completedAt;

	@Transient
	private List<RewardMember> rewardMembers;

	private QuestMemberVerification(
		QuestMember questMember,
		LocalDate verifiedDate,
		QuestMemberVerificationStatus status,
		String contentType,
		String contentUrl,
		String message,
		LocalDateTime completedAt
	) {
		this.questMember = questMember;
		this.verifiedDate = verifiedDate;
		this.status = status;
		this.contentType = contentType;
		this.contentUrl = contentUrl;
		this.message = message;
		this.completedAt = completedAt;
	}

	public static QuestMemberVerification of(
		QuestMember questMember,
		LocalDate verifiedDate,
		QuestMemberVerificationStatus status,
		String contentType,
		String contentUrl,
		String message,
		LocalDateTime completedAt
	) {
		return new QuestMemberVerification(questMember, verifiedDate, status, contentType, contentUrl, message, completedAt);
	}

}
