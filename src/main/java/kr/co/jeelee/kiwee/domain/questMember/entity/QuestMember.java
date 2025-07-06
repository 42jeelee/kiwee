package kr.co.jeelee.kiwee.domain.questMember.entity;

import java.time.LocalDateTime;
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
import kr.co.jeelee.kiwee.domain.questMember.exception.QuestAlreadyEvaluatedException;
import kr.co.jeelee.kiwee.domain.questMember.exception.QuestCantStartException;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberStatus;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.model.MediaType;
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
	private LocalDateTime startDateTime;

	@Column
	private LocalDateTime endDateTime;

	@Column
	private LocalDateTime completedAt;

	@Column
	private MediaType mediaType;

	@Column
	private String mediaUrl;

	@Column
	private String message;

	@Column(nullable = false)
	private Boolean autoReschedule;

	private QuestMember(
		Quest quest, Member member, QuestMemberStatus status,
		LocalDateTime startDateTime, LocalDateTime endDateTime,
		MediaType mediaType, String mediaUrl, String message, Boolean autoReschedule
	) {
		this.quest = quest;
		this.member = member;
		this.status = status;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.completedAt = null;
		this.mediaType = mediaType;
		this.mediaUrl = mediaUrl;
		this.message = message;
		this.autoReschedule = autoReschedule;
	}

	public static QuestMember of(
		Quest quest, Member member, LocalDateTime startDateTime, LocalDateTime endDateTime, Boolean autoReschedule
	) {
		return new QuestMember(
			quest, member, QuestMemberStatus.PLANNED, startDateTime, endDateTime,
			null, null, null, autoReschedule
		);
	}

	public static QuestMember of(
		Quest quest, Member member, QuestMemberStatus status,
		MediaType mediaType, String mediaUrl, String message
	) {
		return new QuestMember(
			quest, member, status, LocalDateTime.now(), null,
			mediaType, mediaUrl, message, false
		);
	}

	public void updatePeriod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		if (startDateTime == null) {
			throw new FieldValidationException("startDateTime", "시작 시간은 비어있을 수 없습니다.");
		}
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
	}

	public void start() {
		if (startDateTime.isAfter(LocalDateTime.now())) {
			throw new QuestCantStartException();
		}
		this.status = QuestMemberStatus.IN_PROGRESS;
	}

	public void complete(MediaType mediaType, String mediaUrl, String message) {
		if (this.status != QuestMemberStatus.PLANNED) {
			throw new QuestAlreadyEvaluatedException();
		}
		this.status = QuestMemberStatus.SUCCEEDED;
		this.mediaType = mediaType;
		this.mediaUrl = mediaUrl;
		this.message = message;
		this.completedAt = LocalDateTime.now();
	}

	public void failed() {
		if (this.status != QuestMemberStatus.PLANNED) {
			throw new QuestAlreadyEvaluatedException();
		}
		this.status = QuestMemberStatus.FAILED;
		this.completedAt = LocalDateTime.now();
	}

	public void switchAutoReschedule(Boolean autoReschedule) {
		this.autoReschedule = autoReschedule;
	}

}
