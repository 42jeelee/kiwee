package kr.co.jeelee.kiwee.global.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RewardMatchPolicy {
	EXACT("자신만"),
	ANY("아무거나"),
	ALL("모두"),
	;
	private final String label;
}
