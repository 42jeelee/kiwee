package kr.co.jeelee.kiwee.domain.questMember.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class QuestMemberVerificationNotFoundException extends CustomException {

	public QuestMemberVerificationNotFoundException() {
		super(ErrorCode.QUEST_VERIFICATION_NOT_FOUND);
	}
}
