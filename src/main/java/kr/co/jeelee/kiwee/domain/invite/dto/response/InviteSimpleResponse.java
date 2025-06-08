package kr.co.jeelee.kiwee.domain.invite.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.invite.entity.Invite;
import kr.co.jeelee.kiwee.domain.invite.model.InviteStatus;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;

public record InviteSimpleResponse(
	UUID id, DomainType domain, Object target,
	MemberSimpleResponse inviter, MemberSimpleResponse invitee,
	InviteStatus status, LocalDateTime expiredAt
) {
	public static InviteSimpleResponse from(Invite invite, Object target) {
		return new InviteSimpleResponse(
			invite.getId(),
			invite.getDomain(),
			target,
			MemberSimpleResponse.from(invite.getInviter()),
			invite.getInvitee() != null
				? MemberSimpleResponse.from(invite.getInvitee())
				: null,
			invite.getStatus(),
			invite.getExpiredAt()
		);
	}
}
