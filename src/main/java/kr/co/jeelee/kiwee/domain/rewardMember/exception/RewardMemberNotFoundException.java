package kr.co.jeelee.kiwee.domain.rewardMember.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class RewardMemberNotFoundException extends CustomException {

	public RewardMemberNotFoundException() {
		super(ErrorCode.REWARD_MEMBER_NOT_FOUND);
	}
}
