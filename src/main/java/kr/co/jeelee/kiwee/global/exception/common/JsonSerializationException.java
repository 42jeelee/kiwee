package kr.co.jeelee.kiwee.global.exception.common;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class JsonSerializationException extends CustomException {

	public JsonSerializationException() {
		super(ErrorCode.KNOWN, Map.of("message", "데이터를 JSON 으로 변화하는 도중 문제가 발생하였습니다."));
	}
}
