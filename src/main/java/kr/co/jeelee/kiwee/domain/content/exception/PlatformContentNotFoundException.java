package kr.co.jeelee.kiwee.domain.content.exception;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class PlatformContentNotFoundException extends CustomException {

	public PlatformContentNotFoundException() {
		super(ErrorCode.NOT_FOUND, Map.of("message", "해당 컨텐트는 해당 플랫폼에 연결되어 있지 않습니다."));
	}
}
