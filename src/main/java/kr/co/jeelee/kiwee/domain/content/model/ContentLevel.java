package kr.co.jeelee.kiwee.domain.content.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentLevel {
	ROOT("작품", 1),
	SEASON("시즌", 2),
	EPISODE("에피소드", 3),
	;
	private final String label;
	private final int depth;
}
