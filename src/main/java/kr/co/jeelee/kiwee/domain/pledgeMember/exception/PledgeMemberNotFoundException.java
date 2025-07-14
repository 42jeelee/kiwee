package kr.co.jeelee.kiwee.domain.pledgeMember.exception;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class PledgeMemberNotFoundException extends CustomException {

	public PledgeMemberNotFoundException() {
		super(ErrorCode.NOT_FOUND, Map.of("message", "해당 약속을 한 맴버가 존재하지 않습니다."));
	}
}
