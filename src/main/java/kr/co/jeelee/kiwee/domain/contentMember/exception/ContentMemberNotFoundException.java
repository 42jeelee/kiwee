package kr.co.jeelee.kiwee.domain.contentMember.exception;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class ContentMemberNotFoundException extends CustomException {

	public ContentMemberNotFoundException() {
		super(ErrorCode.NOT_FOUND, Map.of("message", "등록된 기록이 존재하지 않습니다."));
	}
}
