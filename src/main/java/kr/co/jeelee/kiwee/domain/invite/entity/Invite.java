package kr.co.jeelee.kiwee.domain.invite.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.Type;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.invite.dto.InviteCondition;
import kr.co.jeelee.kiwee.domain.invite.model.InviteStatus;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invites")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invite extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private DomainType domain;

	@Column(nullable = false)
	private UUID targetId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inviter_id", nullable = false)
	private Member inviter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invitee_id")
	private Member invitee;

	@Column(length = 24, nullable = false)
	private String code;

	@Column(nullable = false)
	private InviteStatus status;

	@Column
	private String message;

	@Type(JsonType.class)
	@Column(columnDefinition = "jsonb")
	private InviteCondition condition;

	@Column(nullable = false)
	private int maxUses;

	@Column(nullable = false)
	private int useCount;

	@Column(nullable = false)
	private LocalDateTime expiredAt;

	private Invite(
		DomainType domain, UUID targetId, Member inviter,
		Member invitee, String code, InviteStatus status, String message,
		InviteCondition condition, int maxUses, int useCount
	) {
		this.domain = domain;
		this.targetId = targetId;
		this.inviter = inviter;
		this.invitee = invitee;
		this.code = code;
		this.status = status;
		this.message = message;
		this.condition = condition;
		this.maxUses = maxUses;
		this.useCount = useCount;
		this.expiredAt = LocalDateTime.now().plusDays(7);
	}

	public static Invite of(
		DomainType domain, UUID targetId, Member inviter,
		Member invitee, String code, InviteStatus status, String message,
		InviteCondition condition, int maxUses, int useCount
	) {
		return new Invite(
			domain,  targetId, inviter, invitee, code,
			status, message, condition, maxUses, useCount
		);
	}

	public void accept() {
		if (status == InviteStatus.ACCEPTED) {
			throw new AccessDeniedException("이미 사용된 초대장입니다.");
		}
		if (expiredAt.isBefore(LocalDateTime.now())) {
			throw new AccessDeniedException("유효기한이 지난 초대장입니다.");
		}
		if (!(status == InviteStatus.PENDING) || maxUses != 0 && useCount >= maxUses) {
			throw new AccessDeniedException("이미 모두 소진된 초대장입니다.");
		}

		useCount += 1;

		if (useCount >= maxUses) {
			status = InviteStatus.ACCEPTED;
		}

	}

	public void reject() {
		if (!(status == InviteStatus.PENDING)) {
			throw new  AccessDeniedException("이미 유효하지 않는 초대장입니다.");
		}
		this.status = InviteStatus.REJECTED;
	}

	public void expire() {
		if (!(status == InviteStatus.PENDING)) {
			throw new  AccessDeniedException("이미 유효하지 않는 초대장입니다.");
		}
		this.status = InviteStatus.EXPIRED;
	}

}
