package kr.co.jeelee.kiwee.domain.memberTheme.exception;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class MemberThemeNotFoundException extends CustomException {

	public MemberThemeNotFoundException() {
		super(ErrorCode.NOT_FOUND, Map.of("message", "맴버의 테마가 없습니다."));
	}
}
