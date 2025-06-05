package kr.co.jeelee.kiwee.domain.auth.exception;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class InvalidTokenException extends CustomException {

	public InvalidTokenException() {
		super(ErrorCode.UNAUTHORIZED, Map.of("token", "유효하지 않는 토큰입니다."));
	}
}
