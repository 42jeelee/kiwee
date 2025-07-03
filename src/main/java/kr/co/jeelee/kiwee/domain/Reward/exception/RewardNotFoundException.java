package kr.co.jeelee.kiwee.domain.Reward.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class RewardNotFoundException extends CustomException {

	public RewardNotFoundException() {
		super(ErrorCode.REWARD_NOT_FOUND);
	}
}
