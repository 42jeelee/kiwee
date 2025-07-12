package kr.co.jeelee.kiwee.domain.authorization.dto.response;

import kr.co.jeelee.kiwee.domain.authorization.entity.Permission;
import kr.co.jeelee.kiwee.global.model.PermissionType;

public record PermissionResponse(
	Long id, PermissionType name, String description
) {
	public static PermissionResponse from(Permission permission) {
		return new PermissionResponse(
			permission.getId(),
			permission.getName(),
			permission.getDescription()
		);
	}
}
