package kr.co.jeelee.kiwee.domain.Reward.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RewardType {
	NONE("없음"),
	BADGE("배지"),
	;

	private final String label;
}
