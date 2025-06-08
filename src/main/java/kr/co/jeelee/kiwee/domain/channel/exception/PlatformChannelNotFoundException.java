package kr.co.jeelee.kiwee.domain.channel.exception;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class PlatformChannelNotFoundException extends CustomException {
	public PlatformChannelNotFoundException() {
		super(ErrorCode.CHANNEL_NOT_FOUND, Map.of("platformChannel", "플랫폼과 채널이 연결되어 있지 않습니다."));
	}
}
