package kr.co.jeelee.kiwee.domain.task.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.task.dto.request.TaskCreateRequest;
import kr.co.jeelee.kiwee.domain.task.dto.response.TaskResponse;
import kr.co.jeelee.kiwee.domain.task.entity.Task;
import kr.co.jeelee.kiwee.domain.task.model.TaskType;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface TaskService {

	TaskResponse createTask(UUID memberId, TaskCreateRequest request);

	TaskResponse getTask(UUID id);

	PagedResponse<TaskResponse> getTasks(UUID memberId, TaskType taskType, LocalDate date, Pageable pageable);

	PagedResponse<TaskResponse> getPlayTasksByApplicationId(String applicationId, Pageable pageable);

	Task getById(UUID id);

}
