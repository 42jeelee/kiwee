package kr.co.jeelee.kiwee.global.exception.common;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class InvalidParameterException extends CustomException {

	public InvalidParameterException(String parameterName) {
		super(ErrorCode.REQUEST_INVALID, Map.of(parameterName, "잘못된 인자값 입니다."));
	}
}
