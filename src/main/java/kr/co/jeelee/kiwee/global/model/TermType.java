package kr.co.jeelee.kiwee.global.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TermType {
	NONE("반복없음", null),
	DAILY("매일", "0 0 0 * * *"),
	WEEKLY("주간", "0 0 0 * * MON"),
	MONTHLY("월간", "0 0 0 1 * *"),
	YEARLY("연간", "0 0 0 1 1 *"),
	;

	private final String label;
	private final String cron;
}
