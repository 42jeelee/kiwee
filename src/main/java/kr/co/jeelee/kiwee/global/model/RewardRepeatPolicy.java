package kr.co.jeelee.kiwee.global.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RewardRepeatPolicy {
	ONCE("1회", TermType.NONE),
	DAILY_ONCE("하루 1회", TermType.DAILY),
	WEEKLY_ONCE("주 1회", TermType.WEEKLY),
	MONTHLY_ONCE("월 1회", TermType.MONTHLY),
	EVERY_DAY("매번", null),
	;
	private final String label;
	private final TermType termType;
}
