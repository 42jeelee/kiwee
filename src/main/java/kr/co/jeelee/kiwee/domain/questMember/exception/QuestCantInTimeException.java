package kr.co.jeelee.kiwee.domain.questMember.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class QuestCantInTimeException extends CustomException {

	public QuestCantInTimeException() {
		super(ErrorCode.QUEST_CANT_IN_TIME);
	}
}
