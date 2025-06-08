package kr.co.jeelee.kiwee.global.exception.common;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class AccessDeniedException extends CustomException {

	public AccessDeniedException(String message) {
		super(ErrorCode.ACCESS_DENIED, Map.of("message", message));
	}
}
