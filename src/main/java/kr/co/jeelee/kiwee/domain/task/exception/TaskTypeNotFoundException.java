package kr.co.jeelee.kiwee.domain.task.exception;

import kr.co.jeelee.kiwee.global.exception.custom.CustomException;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;

public class TaskTypeNotFoundException extends CustomException {

	public TaskTypeNotFoundException() {
		super(ErrorCode.TASK_TYPE_NOT_FOUND);
	}
}
