package kr.co.jeelee.kiwee.domain.authorization.dto.request;

import java.util.Set;

import kr.co.jeelee.kiwee.domain.authorization.model.RoleType;

public record RolesRequest(
	Set<RoleType> roles
) {
}
