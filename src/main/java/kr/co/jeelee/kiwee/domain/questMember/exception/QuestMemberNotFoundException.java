package kr.co.jeelee.kiwee.domain.questMember.exception;

import java.util.Map;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class QuestMemberNotFoundException extends CustomException {

	public QuestMemberNotFoundException() {
		super(ErrorCode.QUEST_MEMBER_NOT_FOUND);
	}

	public QuestMemberNotFoundException(String message) {
		super(ErrorCode.QUEST_MEMBER_NOT_FOUND, Map.of("message", message));
	}
}
