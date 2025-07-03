package kr.co.jeelee.kiwee.domain.BadgeMember.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class BadgeMaxLevelException extends CustomException {

	public BadgeMaxLevelException() {
		super(ErrorCode.BADGE_MAX_LEVEL);
	}
}
