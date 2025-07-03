package kr.co.jeelee.kiwee.domain.quest.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class QuestNotFoundException extends CustomException {

	public QuestNotFoundException() {
		super(ErrorCode.QUEST_NOT_FOUND);
	}
}
