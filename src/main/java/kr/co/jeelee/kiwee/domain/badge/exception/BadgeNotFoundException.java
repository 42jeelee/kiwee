package kr.co.jeelee.kiwee.domain.badge.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class BadgeNotFoundException extends CustomException {

	public BadgeNotFoundException() {
		super(ErrorCode.BADGE_NOT_FOUND);
	}
}
