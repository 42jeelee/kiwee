package kr.co.jeelee.kiwee.global.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityType {
	JOIN("가입", true),
	LEAVE("탈퇴", true),
	RECORD("기록", false),
	REMOVE("삭제", false),
	PLAY("활동", false),
	START("시작", true),
	UPDATE("수정", false),
	END("종료", false),
	PASS("통과", false),
	COMPLETE("완료", true),
	FAILED("실패", true),
	LINK("연동", true),
	INVITE("초대", false),
	;

	private final String label;
	private final boolean notify;
}
