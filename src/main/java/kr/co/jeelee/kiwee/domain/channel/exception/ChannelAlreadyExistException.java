package kr.co.jeelee.kiwee.domain.channel.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class ChannelAlreadyExistException extends CustomException {

	public ChannelAlreadyExistException() {
		super(ErrorCode.CHANNEL_ALREADY_EXIST);
	}
}
