package kr.co.jeelee.kiwee.domain.platform.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class PlatformNotFoundException extends CustomException {

	public PlatformNotFoundException() {
		super(ErrorCode.PLATFORM_NOT_FOUND);
	}
}
