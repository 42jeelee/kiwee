package kr.co.jeelee.kiwee.global.exception.common;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class DomainNotFoundException extends CustomException {

	public DomainNotFoundException() {
		super(ErrorCode.DOMAIN_NOT_FOUND);
	}
}
