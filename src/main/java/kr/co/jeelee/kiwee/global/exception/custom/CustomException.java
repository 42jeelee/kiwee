package kr.co.jeelee.kiwee.global.exception.custom;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;
	private final Map<String, String> details;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.details = null;
	}

	public CustomException(ErrorCode errorCode, Map<String, String> details) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.details = details;
	}
}
