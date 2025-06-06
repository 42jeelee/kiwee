package kr.co.jeelee.kiwee.domain.authorization.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class RoleNotFoundException extends CustomException {

	public RoleNotFoundException() {
		super(ErrorCode.ROLE_NOT_FOUND);
	}
}
