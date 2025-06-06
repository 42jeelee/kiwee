package kr.co.jeelee.kiwee.domain.authorization.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class PermissionNotFoundException extends CustomException {

	public PermissionNotFoundException() {
		super(ErrorCode.PERMISSION_NOT_FOUND);
	}
}
