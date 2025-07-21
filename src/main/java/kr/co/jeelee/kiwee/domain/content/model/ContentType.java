package kr.co.jeelee.kiwee.domain.content.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {
	BOOK("책"),
	GAME("게임"),
	ALBUM("앨범"),
	MUSIC("음악"),
	COLLECTION("영화 콜렉션"),
	SERIES("시리즈"),
	SEASON("시즌"),
	MOVIE("영화"),
	EPISODE("에피소드"),
	;

	private final String label;

}
