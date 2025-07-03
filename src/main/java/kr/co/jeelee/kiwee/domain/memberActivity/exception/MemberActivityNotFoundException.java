package kr.co.jeelee.kiwee.domain.memberActivity.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class MemberActivityNotFoundException extends CustomException {

	public MemberActivityNotFoundException() {
		super(ErrorCode.ACTIVITY_NOT_FOUND);
	}
}
