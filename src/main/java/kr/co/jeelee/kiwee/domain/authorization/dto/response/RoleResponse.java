package kr.co.jeelee.kiwee.domain.authorization.dto.response;

import java.util.List;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.authorization.entity.Role;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.authorization.model.RoleType;

public record RoleResponse(
	UUID id, DomainType domain, RoleType name, String color,
	String description, List<PermissionResponse> permissions
) {
	public static RoleResponse from(Role role) {
		return new RoleResponse(
			role.getId(),
			role.getDomain(),
			role.getName(),
			role.getColor(),
			role.getDescription(),
			role.getPermissions().stream()
				.map(PermissionResponse::from)
				.toList()
		);
	}
}
