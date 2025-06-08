package kr.co.jeelee.kiwee.global.exception.custom;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// Common
	REQUEST_INVALID(HttpStatus.BAD_REQUEST, "REQUEST-001", "잘못된 요청입니다."),
	MISSING_PARAMS(HttpStatus.BAD_REQUEST, "REQUEST-002", "필요한 인자가 누락되었습니다."),
	NOT_FOUND(HttpStatus.NOT_FOUND, "REQUEST-002", "요청하신 데이터를 찾을 수 없습니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "REQUEST-003", "지원하지 않는 HTTP 메서드입니다."),
	NOT_SUPPORT_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "REQUEST-004", "지원하지 않는 파일 형식입니다."),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "REQUEST-005", "접근 권한이 없습니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "REQUEST-006", "인증이 필요합니다."),

	UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "REQUEST-004", "알 수 없는 오류가 발생했습니다."),

	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER-001", "해당 유저를 찾을 수 없습니다."),
	MEMBER_NOT_HAVE(HttpStatus.NOT_FOUND, "MEMBER-002", "해당 맴버가 소유하지 않은 자원입니다."),

	REPUTATION_NOT_FOUND(HttpStatus.NOT_FOUND, "REPUTATION-001", "해당 투표 기록이 없습니다."),

	PLATFORM_NOT_FOUND(HttpStatus.NOT_FOUND, "PLATFORM-001", "해당 플랫폼을 찾을 수 없습니다."),
	MEMBER_PLATFORM_NOT_FOUND(HttpStatus.NOT_FOUND, "PLATFORM-002", "해당 가입 정보를 찾을 수 없습니다."),

	DOMAIN_NOT_FOUND(HttpStatus.NOT_FOUND, "DOMAIN-001", "해당 도메인은 존재하지 않습니다."),

	PERMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "PERMISSION-001", "해당 권한은 찾을 수 없습니다."),

	ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ROLE-001", "해당 역할은 찾을 수 없습니다."),

	CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "CHANNEL-001", "해당 채널은 찾을 수 없습니다."),
	CHANNEL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "CHANNEL-002", "이미 존재하는 채널입니다."),

	INVITE_NOT_FOUND(HttpStatus.NOT_FOUND, "INVITE-001", "유효하지 않는 초대코드입니다."),
	;

	private final HttpStatus status;
	private final String code;
	private final String message;

}
