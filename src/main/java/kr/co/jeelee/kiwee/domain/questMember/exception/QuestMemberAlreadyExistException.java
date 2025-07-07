package kr.co.jeelee.kiwee.domain.questMember.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class QuestMemberAlreadyExistException extends CustomException {

	public QuestMemberAlreadyExistException() {
		super(ErrorCode.QUEST_ALREADY_EXIST);
	}
}
