package kr.co.jeelee.kiwee.domain.badge.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class BadgeLevelCantRemoveException extends CustomException {

	public BadgeLevelCantRemoveException() {
		super(ErrorCode.BADGE_LEVEL_CANT_REMOVE);
	}
}
