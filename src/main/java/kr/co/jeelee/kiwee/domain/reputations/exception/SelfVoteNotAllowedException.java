package kr.co.jeelee.kiwee.domain.reputations.exception;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class SelfVoteNotAllowedException extends CustomException {

	public SelfVoteNotAllowedException() {
		super(ErrorCode.REQUEST_INVALID, Map.of("receiverId", "자신에게 투표할 수 없습니다."));
	}
}
