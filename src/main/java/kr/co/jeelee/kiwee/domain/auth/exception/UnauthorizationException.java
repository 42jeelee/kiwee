package kr.co.jeelee.kiwee.domain.auth.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class UnauthorizationException extends CustomException {

	public UnauthorizationException() {
		super(ErrorCode.UNAUTHORIZED);
	}
}
