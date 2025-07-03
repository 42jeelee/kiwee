package kr.co.jeelee.kiwee.domain.Reward.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TriggerType {
	JOIN("가입"),
	INVITE("초대"),
	IN_PROGRESS("시작"),
	SUCCESS("성공"),
	FAILURE("실패"),
	PROGRESS_END("종료"),
	;

	private final String label;
}
