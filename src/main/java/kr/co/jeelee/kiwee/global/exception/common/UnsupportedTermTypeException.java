package kr.co.jeelee.kiwee.global.exception.common;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class UnsupportedTermTypeException extends CustomException {

	public UnsupportedTermTypeException() {
		super(ErrorCode.REQUEST_INVALID, Map.of("message", "지원하지 않는 TermType 입니다."));
	}
}
