package kr.co.jeelee.kiwee.domain.pledgeMember.exception;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class PledgeMemberInvalidException extends CustomException {

	public PledgeMemberInvalidException(String message) {
		super(ErrorCode.REQUEST_INVALID, Map.of("message", message));
	}
}
