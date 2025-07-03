package kr.co.jeelee.kiwee.domain.questMember.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.quest.entity.Quest;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberStatus;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quest_members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestMember extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quest_id")
	private Quest quest;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(nullable = false)
	private QuestMemberStatus status;

	@Column(nullable = false)
	private Integer failCount;

	@Column(nullable = false)
	private LocalDateTime startDate;

	@Column
	private LocalDateTime endDate;

	@Column
	private LocalTime verifiableFrom;

	@Column
	private LocalTime verifiableUntil;

	@Column
	private LocalDateTime deadDate;

	private QuestMember(
		Quest quest, Member member, QuestMemberStatus status, LocalDateTime startDate,
		LocalDateTime endDate, LocalTime verifiableFrom, LocalTime verifiableUntil
	) {
		this.quest = quest;
		this.member = member;
		this.status = status;
		this.failCount = 0;
		this.startDate = startDate;
		this.endDate = endDate;
		this.verifiableFrom = verifiableFrom;
		this.verifiableUntil = verifiableUntil;
		this.deadDate = null;
	}

	public static QuestMember of(
		Quest quest, Member member, LocalDateTime startDate,
		LocalTime verifiableFrom, LocalTime verifiableUntil
	) {
		QuestMemberStatus status = QuestMemberStatus.PLANNED;
		if (!startDate.isAfter(LocalDateTime.now())) {
			status = QuestMemberStatus.IN_PROGRESS;
		}

		LocalDateTime endDate = null;
		if (quest.getCompletedLimit() != null) {
			endDate = startDate.plus(quest.getCompletedLimit());
		}

		return new QuestMember(
			quest, member, status, startDate, endDate, verifiableFrom, verifiableUntil
		);
	}

	public void failure() {
		this.status = QuestMemberStatus.FAILED;
		this.deadDate = LocalDateTime.now();
	}

	public Integer addFailCount() {
		this.failCount++;
		return this.failCount;
	}

}
