package kr.co.jeelee.kiwee.domain.invite.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.invite.entity.Invite;
import kr.co.jeelee.kiwee.domain.invite.model.InviteStatus;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;

public record InviteDetailResponse(
	UUID id, DomainType domain, Object target, MemberSimpleResponse inviterId,
	MemberSimpleResponse inviteeId, String code, InviteStatus status, String message,
	int maxUses, int useCount, LocalDateTime expiredAt, LocalDateTime createdAt
) {
	public static InviteDetailResponse from(Invite invite, Object targetResponse) {
		return new InviteDetailResponse(
			invite.getId(),
			invite.getDomain(),
			targetResponse,
			MemberSimpleResponse.from(invite.getInviter()),
			invite.getInvitee() != null
				? MemberSimpleResponse.from(invite.getInvitee())
				: null,
			invite.getCode(),
			invite.getStatus(),
			invite.getMessage(),
			invite.getMaxUses(),
			invite.getUseCount(),
			invite.getExpiredAt(),
			invite.getCreatedAt()
		);
	}
}
