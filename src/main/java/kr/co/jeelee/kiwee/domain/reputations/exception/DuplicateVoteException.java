package kr.co.jeelee.kiwee.domain.reputations.exception;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class DuplicateVoteException extends CustomException {

	public DuplicateVoteException() {
		super(ErrorCode.REQUEST_INVALID, Map.of("giverId", "이미 투표한 대상입니다."));
	}
}
