package kr.co.jeelee.kiwee.domain.genre.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class GenreNotFoundException extends CustomException {

	public GenreNotFoundException() {
		super(ErrorCode.GENRE_NOT_FOUND);
	}
}
