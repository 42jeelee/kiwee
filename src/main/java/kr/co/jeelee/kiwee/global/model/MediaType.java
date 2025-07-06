package kr.co.jeelee.kiwee.global.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MediaType {
	TEXT("글자"),
	IMAGE("이미지"),
	AUDIO("오디오"),
	VIDEO("영상")
	;
	private final String label;
}
