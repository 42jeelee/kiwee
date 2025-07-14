package kr.co.jeelee.kiwee.domain.authorization.dto.request;

import java.util.Set;

import kr.co.jeelee.kiwee.global.model.RoleType;

public record RolesRequest(
	Set<RoleType> roles
) {
}
