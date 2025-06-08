package kr.co.jeelee.kiwee.domain.channel.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class ChannelNotFoundException extends CustomException {

	public ChannelNotFoundException() {
		super(ErrorCode.CHANNEL_NOT_FOUND);
	}
}
