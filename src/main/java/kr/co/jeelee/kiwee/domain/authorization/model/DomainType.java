package kr.co.jeelee.kiwee.domain.authorization.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DomainType {
	GLOBAL("글로벌"),
	PLATFORM("플랫폼"),
	;

	private final String label;

	@JsonCreator
	public static DomainType from(String name) {
		return DomainType.valueOf(name.toUpperCase());
	}

	@JsonValue
	public String toJson() {
		return name().toLowerCase();
	}

}