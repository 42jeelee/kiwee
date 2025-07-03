package kr.co.jeelee.kiwee.domain.badge.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class BadgeLevelAlreadyExistException extends CustomException {

	public BadgeLevelAlreadyExistException() {
		super(ErrorCode.BADGE_LEVEL_ALREADY_EXIST);
	}
}
