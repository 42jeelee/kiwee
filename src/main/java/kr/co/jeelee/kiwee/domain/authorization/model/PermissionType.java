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

	ROLE_CREATE_CHANNEL(DomainType.GLOBAL, "채널 생성"),
	ROLE_READ_CHANNEL(DomainType.GLOBAL, "모든 채널 읽기"),

	ROLE_INVITE_EXPIRED(DomainType.GLOBAL, "초대 만료 권한"),

	ROLE_OTHER_NOTIFICATION(DomainType.GLOBAL, "다른 유저 알림 읽기"),
	ROLE_OTHER_ACTIVITY(DomainType.GLOBAL, "다른 유저 활동 읽기"),

	// Platform
	ROLE_CREATE_PLATFORM(DomainType.PLATFORM, "플랫폼 생성"),
	ROLE_EDIT_PLATFORM(DomainType.PLATFORM, "플랫폼 수정"),
	ROLE_DELETE_PLATFORM(DomainType.PLATFORM, "플랫폼 삭제"),

	// Channel
	ROLE_EDIT_CHANNEL(DomainType.CHANNEL, "채널 수정"),
	ROLE_DELETE_CHANNEL(DomainType.CHANNEL, "채널 삭제"),
	ROLE_CHANNEL_PERMISSION_GRANTER(DomainType.CHANNEL, "채널 권한 관리."),
	ROLE_CHANNEL_BEN_GRANTER(DomainType.CHANNEL, "상대 벤 가능."),
	ROLE_CHANNEL_INVITE_MEMBER(DomainType.CHANNEL, "채널에 맴버 초대 가능."),
	ROLE_CHANNEL_KICK_MEMBER(DomainType.CHANNEL, "채널에서 맴버 강퇴 가능."),
	ROLE_CHANNEL_CREATE_QUEST(DomainType.CHANNEL, "채널 퀘스트 생성 가능."),
	ROLE_CHANNEL_MAKE_REWARD(DomainType.CHANNEL, "채널 보상 생성 가능"),

	// Reward
	ROLE_CREATE_REWARD(DomainType.REWARD, "보상 생성"),
	ROLE_DELETE_REWARD(DomainType.REWARD, "보상 삭제"),

	// Quest
	ROLE_CREATE_QUEST(DomainType.QUEST, "퀘스트 생성"),
	ROLE_EDIT_QUEST(DomainType.QUEST, "퀘스트 수정"),
	ROLE_DELETE_QUEST(DomainType.QUEST, "퀘스트 삭제"),

	// Badge
	ROLE_CREATE_BADGE(DomainType.BADGE, "배지 생성"),
	ROLE_EDIT_BADGE(DomainType.BADGE, "배지 수정"),
	ROLE_DELETE_BADGE(DomainType.BADGE, "배지 삭제"),

	// Content
	ROLE_CREATE_CONTENT(DomainType.CONTENT, "컨텐츠 생성"),
	ROLE_EDIT_CONTENT(DomainType.CONTENT, "컨텐츠 수정"),
	ROLE_DELETE_CONTENT(DomainType.CONTENT, "컨텐츠 삭제"),
	ROLE_CREATE_GENRE(DomainType.CONTENT, "장르 생성"),
	ROLE_EDIT_GENRE(DomainType.CONTENT, "장르 수정"),
	ROLE_DELETE_GENRE(DomainType.CONTENT, "장르 삭제"),

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
