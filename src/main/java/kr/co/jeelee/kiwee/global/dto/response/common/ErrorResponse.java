package kr.co.jeelee.kiwee.global.dto.response.common;

import java.util.Map;

public record ErrorResponse(String code, String message, Map<String, String> details) {
	public static ErrorResponse of(String code, String message, Map<String, String> details) {
		return new ErrorResponse(code, message, details);
	}
}
