package kr.co.jeelee.kiwee.domain.BadgeMember.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class BadgeMemberNotFoundException extends CustomException {

	public BadgeMemberNotFoundException() {
		super(ErrorCode.BADGE_MEMBER_NOT_FOUND);
	}
}
