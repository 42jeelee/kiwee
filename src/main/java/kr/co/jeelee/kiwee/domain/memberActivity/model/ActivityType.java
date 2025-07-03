package kr.co.jeelee.kiwee.domain.memberActivity.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityType {
	JOIN("가입"),
	VERIFY("인증"),
	LINK("연동"),
	INVITE("초대"),
	EVENT("이벤트"),
	;

	private final String label;
}
