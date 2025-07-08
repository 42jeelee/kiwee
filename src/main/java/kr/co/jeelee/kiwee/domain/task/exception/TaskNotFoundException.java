package kr.co.jeelee.kiwee.domain.task.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class TaskNotFoundException extends CustomException {

	public TaskNotFoundException() {
		super(ErrorCode.TASK_NOT_FOUND);
	}
}
