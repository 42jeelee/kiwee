package kr.co.jeelee.kiwee.global.dto.response;

import java.time.LocalDateTime;

public record GlobalResponse<T>(boolean isSuccess, T data, LocalDateTime timestamp) {

	public static <T> GlobalResponse<T> ok(T data) {
		return new GlobalResponse<T>(true, data, LocalDateTime.now());
	}

	public static GlobalResponse<ErrorResponse> error(ErrorResponse data) {
		return new GlobalResponse<ErrorResponse>(false, data, LocalDateTime.now());
	}
}
