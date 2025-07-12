package kr.co.jeelee.kiwee.domain.pledge.exception;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class PledgeNotFoundException extends CustomException {

	public PledgeNotFoundException() {
		super(ErrorCode.NOT_FOUND, Map.of("message", "요청하신 약속을 찾을 수 없습니다."));
	}
}
