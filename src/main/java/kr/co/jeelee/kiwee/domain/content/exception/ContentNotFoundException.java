package kr.co.jeelee.kiwee.domain.content.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class ContentNotFoundException extends CustomException {

	public ContentNotFoundException() {
		super(ErrorCode.CONTENT_NOT_FOUND);
	}
}
