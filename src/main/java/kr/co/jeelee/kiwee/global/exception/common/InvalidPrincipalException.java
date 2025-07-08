package kr.co.jeelee.kiwee.global.exception.common;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class InvalidPrincipalException extends CustomException {

	public InvalidPrincipalException() {
		super(ErrorCode.UNAUTHORIZED, Map.of("message", "로그인 정보가 유효하지 않습니다."));
	}
}
