package kr.co.jeelee.kiwee.domain.memberActivity.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityType {
	JOIN("가입"),
	LEAVE("탈퇴"),
	RECORD("기록"),
	REMOVE("삭제"),
	COMPLETE("완료"),
	FAILED("실패"),
	LINK("연동"),
	INVITE("초대"),
	;

	private final String label;
}
