package kr.co.jeelee.kiwee.domain.questMember.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class QuestAlreadyEvaluatedException extends CustomException {

	public QuestAlreadyEvaluatedException() {
		super(ErrorCode.QUEST_ALREADY_EVALUATED);
	}
}
