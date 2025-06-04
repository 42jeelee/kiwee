package kr.co.jeelee.kiwee.domain.auth.oauth.exception;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class ProviderNotFoundException extends CustomException {

	public ProviderNotFoundException() {
		super(ErrorCode.REQUEST_INVALID, Map.of("provider", "로그인을 제공하는 플랫폼이 아닙니다."));
	}
}
