package kr.co.jeelee.kiwee.domain.member.exception;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class MemberNotHaveRoleException extends CustomException {

	public MemberNotHaveRoleException() {
		super(ErrorCode.MEMBER_NOT_HAVE, Map.of("role", "맴버가 소유하지 않은 역할입니다."));
	}
}
