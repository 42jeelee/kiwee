package kr.co.jeelee.kiwee.domain.questMember.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class QuestCantStartException extends CustomException {

	public QuestCantStartException() {
		super(ErrorCode.QUEST_CANT_START);
	}
}
