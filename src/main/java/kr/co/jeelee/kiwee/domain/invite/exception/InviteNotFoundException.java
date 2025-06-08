package kr.co.jeelee.kiwee.domain.invite.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class InviteNotFoundException extends CustomException {

	public InviteNotFoundException() {
		super(ErrorCode.INVITE_NOT_FOUND);
	}
}
