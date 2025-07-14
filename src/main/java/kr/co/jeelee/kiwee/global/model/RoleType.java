package kr.co.jeelee.kiwee.global.model;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
	ADMIN("FF0034", DomainType.GLOBAL,"관리자", List.of(PermissionType.values())),
	MEMBER("EFD141", DomainType.GLOBAL,"맴버", List.of(PermissionType.ROLE_CREATE_REPUTATION)),
	BOT("5788F6", DomainType.GLOBAL,"봇", List.of(
		PermissionType.ROLE_CREATE_MEMBER, PermissionType.ROLE_EDIT_MEMBER,
		PermissionType.ROLE_CREATE_PLATFORM, PermissionType.ROLE_EDIT_PLATFORM,
		PermissionType.ROLE_READ_MEMBER, PermissionType.ROLE_READ_OTHER_REPUTATION
	)),

	ANALYST("9D67F0", DomainType.GLOBAL,"상대 인기도 기록 볼 수 있음", List.of(PermissionType.ROLE_READ_OTHER_REPUTATION)),

	PLATFORM_CREATOR("38CF4C",DomainType.PLATFORM, "플랫폼 생성 가능", List.of(PermissionType.ROLE_CREATE_PLATFORM)),
	PLATFORM_MODERATOR("B3706E", DomainType.PLATFORM,"플랫폼 수정 및 삭제 가능", List.of(
		PermissionType.ROLE_EDIT_PLATFORM, PermissionType.ROLE_DELETE_PLATFORM
	)),

	CHANNEL_MANAGER("02867D", DomainType.CHANNEL, "채널 메니저", Arrays.stream(PermissionType.values())
		.filter(p -> p.getDomainType().equals(DomainType.CHANNEL))
		.toList()
	),
	CHANNEL_EDITOR("A394B1", DomainType.CHANNEL, "채널 수정 가능", List.of(PermissionType.ROLE_EDIT_CHANNEL)),
	CHANNEL_BREAKER("DB0170", DomainType.CHANNEL, "채널 삭제 가능", List.of(PermissionType.ROLE_DELETE_CHANNEL)),
	CHANNEL_MEMBER("FAFFCA", DomainType.CHANNEL, "채널 맴버", List.of());
	;

	private final String color;
	private final DomainType domain;
	private final String description;
	private final List<PermissionType> permissions;

	@JsonCreator
	public static RoleType from(String name) {
		return RoleType.valueOf(name.toUpperCase());
	}

	@JsonValue
	public String toJson() {
		return name().toLowerCase();
	}

}
