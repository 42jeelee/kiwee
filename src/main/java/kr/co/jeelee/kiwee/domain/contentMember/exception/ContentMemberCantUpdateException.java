package kr.co.jeelee.kiwee.domain.contentMember.exception;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class ContentMemberCantUpdateException extends CustomException {

	public ContentMemberCantUpdateException() {
		super(ErrorCode.REQUEST_INVALID, Map.of("message", "리뷰가 존재하면 수정할 수 없습니다."));
	}
}
