package kr.co.jeelee.kiwee.domain.invite.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.domain.invite.dto.InviteCondition;

public record InviteCreateRequest(
	@NotNull(message = "domain can't be Null.") DomainType domain,
	@NotNull(message = "targetId can't be Null.") UUID targetId,
	UUID inviteeId,
	String message, InviteCondition condition, int maxUses
) {
}
