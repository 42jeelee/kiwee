package kr.co.jeelee.kiwee.domain.reputations.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class ReputationNotFoundException extends CustomException {

	public ReputationNotFoundException() {
		super(ErrorCode.REPUTATION_NOT_FOUND);
	}
}
