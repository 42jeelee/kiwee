package kr.co.jeelee.kiwee.domain.authorization.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionType {
	// Global
	ROLE_PERMISSION_GRANTER(DomainType.GLOBAL, "유저 권한 관리"),

	ROLE_CREATE_MEMBER(DomainType.GLOBAL, "다른 유저 생성"),
	ROLE_READ_MEMBER(DomainType.GLOBAL, "다른 유저 읽기"),
	ROLE_EDIT_MEMBER(DomainType.GLOBAL, "다른 유저 수정"),
	ROLE_DELETE_MEMBER(DomainType.GLOBAL, "다른 유저 삭제"),

	ROLE_CREATE_REPUTATION(DomainType.GLOBAL, "인기 투표 가능"),
	ROLE_READ_OTHER_REPUTATION(DomainType.GLOBAL, "다른 사람의 인기투표 기록 확인 가능"),

	// Platform
	ROLE_CREATE_PLATFORM(DomainType.PLATFORM, "플랫폼 생성"),
	ROLE_EDIT_PLATFORM(DomainType.PLATFORM, "플랫폼 수정"),
	ROLE_DELETE_PLATFORM(DomainType.PLATFORM, "플랫폼 삭제"),
	;

	private final DomainType domainType;
	private final String description;

	@JsonCreator
	public static PermissionType from(String name) {
		return PermissionType.valueOf(name.toUpperCase());
	}

	@JsonValue
	public String toJson() {
		return name().toLowerCase();
	}

}
