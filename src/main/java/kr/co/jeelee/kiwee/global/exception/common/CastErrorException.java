package kr.co.jeelee.kiwee.global.exception.common;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class CastErrorException extends CustomException {

	public <T> CastErrorException(Class<T> clazz) {
		super(ErrorCode.KNOWN, Map.of(clazz.getName(), "해당 클래스로 캐스팅할 수 없습니다."));
	}
}
