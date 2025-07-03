package kr.co.jeelee.kiwee.domain.badge.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BadgeGrade {
	NORMAL("노멀"),
	RARE("레어"),
	EPIC("에픽"),
	LEGENDARY("레전더리"),
	MYTHIC("신화"),
	;

	private final String label;
}
