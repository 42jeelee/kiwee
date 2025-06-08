package kr.co.jeelee.kiwee.domain.member.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import kr.co.jeelee.kiwee.domain.authorization.model.RoleType;

public record MemberRolesRequest(
	@NotEmpty(message = "There must be at least one roles.") List<RoleType> roles
) {
}
