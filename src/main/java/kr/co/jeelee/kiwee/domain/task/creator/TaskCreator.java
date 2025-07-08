package kr.co.jeelee.kiwee.domain.task.creator;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.task.dto.request.TaskCreateRequest;
import kr.co.jeelee.kiwee.domain.task.entity.Task;
import kr.co.jeelee.kiwee.domain.task.model.TaskType;

public interface TaskCreator<T extends TaskCreateRequest> {

	Task create(UUID channelId, UUID memberId, T request);

	TaskType supports();

	Class<? extends TaskCreateRequest> supportsClass();

}
