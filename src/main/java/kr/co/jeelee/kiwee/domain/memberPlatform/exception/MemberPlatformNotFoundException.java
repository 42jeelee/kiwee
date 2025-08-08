package kr.co.jeelee.kiwee.domain.memberPlatform.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class MemberPlatformNotFoundException extends CustomException {
	public MemberPlatformNotFoundException() {
		super(ErrorCode.MEMBER_PLATFORM_NOT_FOUND);
	}
}
