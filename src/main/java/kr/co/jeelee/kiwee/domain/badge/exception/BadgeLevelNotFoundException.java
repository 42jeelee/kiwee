package kr.co.jeelee.kiwee.domain.badge.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class BadgeLevelNotFoundException extends CustomException {

	public BadgeLevelNotFoundException() {
		super(ErrorCode.BADGE_LEVEL_NOT_FOUND);
	}
}
