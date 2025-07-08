package kr.co.jeelee.kiwee.domain.task.creator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import kr.co.jeelee.kiwee.domain.task.dto.request.TaskCreateRequest;
import kr.co.jeelee.kiwee.domain.task.entity.Task;
import kr.co.jeelee.kiwee.domain.task.exception.TaskTypeNotFoundException;

@Component
public class TaskCreatorDispatcher {

	private final Map<Class<? extends TaskCreateRequest>, TaskCreator<?>> creators = new HashMap<>();

	public TaskCreatorDispatcher(List<TaskCreator<?>> creators) {
		for (TaskCreator<?> creator : creators) {
			this.creators.put(creator.supportsClass(), creator);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends TaskCreateRequest> Task create(UUID channelId, UUID memberId, T request) {
		TaskCreator<T> creator = (TaskCreator<T>) creators.get(request.getClass());
		if (creator == null) {
			throw new TaskTypeNotFoundException();
		}
		return creator.create(channelId, memberId, request);
	}

}
