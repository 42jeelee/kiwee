package kr.co.jeelee.kiwee.domain.review.exception;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class ReviewNotFoundException extends CustomException {

	public ReviewNotFoundException() {
		super(ErrorCode.NOT_FOUND, Map.of("message", "해당 리뷰가 존재하지 않습니다."));
	}
}
