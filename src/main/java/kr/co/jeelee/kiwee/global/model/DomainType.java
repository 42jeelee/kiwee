package kr.co.jeelee.kiwee.global.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import kr.co.jeelee.kiwee.global.exception.common.CastErrorException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DomainType {
	GLOBAL("글로벌", "globals"),
	PLATFORM("플랫폼", "platforms"),
	CHANNEL("채널", "channels"),
	MEMBER("맴버", "members"),
	REWARD("보상", "rewards"),
	TASK("작업", "tasks"),
	PLEDGE("약속", "pledges"),
	BADGE("배지", "badges"),
	CONTENT("컨텐츠", "contents"),
	;

	private final String label;
	private final String path;

	@JsonCreator
	public static DomainType from(String path) {
		return Arrays.stream(values())
			.filter(domainType -> domainType.getPath().equalsIgnoreCase(path))
			.findFirst()
			.orElseThrow(() -> new CastErrorException(DomainType.class));
	}

	@JsonValue
	public String toJson() {
		return path.toLowerCase();
	}

}