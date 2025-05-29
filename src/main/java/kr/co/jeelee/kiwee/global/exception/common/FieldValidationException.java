package kr.co.jeelee.kiwee.global.exception.common;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class FieldValidationException extends CustomException {
	public FieldValidationException(String field, String message) {
		super(ErrorCode.REQUEST_INVALID, Map.of(field, message));
	}
}
