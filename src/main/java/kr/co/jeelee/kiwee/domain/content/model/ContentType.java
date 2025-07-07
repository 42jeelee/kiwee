package kr.co.jeelee.kiwee.domain.content.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {
	BOOK("책"),
	GAME("게임"),
	MOVIE("영화"),
	DRAMA("드라마"),
	ANIMATION("애니메이션"),
	;

	private final String label;

}
