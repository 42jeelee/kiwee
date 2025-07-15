package kr.co.jeelee.kiwee.domain.task.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.model.PermissionType;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.memberActivity.event.MemberActivityEvent;
import kr.co.jeelee.kiwee.domain.task.creator.TaskCreatorDispatcher;
import kr.co.jeelee.kiwee.domain.task.dto.request.TaskCreateRequest;
import kr.co.jeelee.kiwee.domain.task.dto.response.TaskResponse;
import kr.co.jeelee.kiwee.domain.task.entity.Task;
import kr.co.jeelee.kiwee.domain.task.exception.TaskNotFoundException;
import kr.co.jeelee.kiwee.domain.task.model.TaskType;
import kr.co.jeelee.kiwee.domain.task.repository.TaskRepository;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;

	private final TaskCreatorDispatcher taskCreatorDispatcher;

	private final MemberService memberService;

	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public TaskResponse createTask(UUID memberId, TaskCreateRequest request) {
		validateHasCreateAuthority(memberId);
		Task task = taskCreatorDispatcher.create(memberId, request);
		Task savedTask = taskRepository.save(task);

		eventPublisher.publishEvent(MemberActivityEvent.of(
			memberId,
			DomainType.TASK,
			savedTask.getId(),
			task.getActivityType()
		));

		return TaskResponse.from(savedTask);
	}

	@Override
	public TaskResponse getTask(UUID id) {
		Task task = taskRepository.findById(id)
			.orElseThrow(TaskNotFoundException::new);

		return TaskResponse.from(task);
	}

	@Override
	public PagedResponse<TaskResponse> getTasks(UUID memberId, TaskType taskType, LocalDate date,
		Pageable pageable) {
		Member member = memberId != null
			? memberService.getById(memberId)
			: null;

		return PagedResponse.of(
			fetchTasks(member, taskType, date, pageable),
			TaskResponse::from
		);
	}

	@Override
	public PagedResponse<TaskResponse> getPlayTasksByApplicationId(String applicationId, Pageable pageable) {
		return PagedResponse.of(
			taskRepository.findPlayTaskByApplicationId(
				TaskType.PLAY,
				applicationId,
				pageable
			),
			TaskResponse::from
		);
	}

	@Override
	public Task getById(UUID id) {
		return taskRepository.findById(id)
			.orElseThrow(TaskNotFoundException::new);
	}

	private void validateHasCreateAuthority(UUID memberId) {
		if (!(
			SecurityUtil.hasAuthority(PermissionType.ROLE_CREATE_OTHER_TASK)
			|| SecurityUtil.getLoginMember().getId().equals(memberId)
		)) {
			throw new AccessDeniedException("해당 회원이 아니거나 권한이 없습니다.");
		}
	}

	private Page<Task> fetchTasks(
		Member member,
		TaskType taskType,
		LocalDate date,
		Pageable pageable
	) {
		boolean hasMember = member != null;
		boolean hasTaskType = taskType != null;
		boolean hasDate = date != null;

		LocalDateTime start = hasDate
			? date.atStartOfDay()
			: null;
		LocalDateTime end = hasDate
			? date.plusDays(1).atStartOfDay()
			: null;

		if (hasMember && hasTaskType && hasDate) {
			return taskRepository.findByMemberAndTaskTypeAndCreatedAtBetween(
				member,
				taskType,
				start,
				end,
				pageable
			);
		}

		if (hasMember && hasTaskType) {
			return taskRepository.findByMemberAndTaskType(
				member,
				taskType,
				pageable
			);
		}

		if (hasTaskType && hasDate) {
			return taskRepository.findByTaskTypeAndCreatedAtBetween(
				taskType,
				start,
				end,
				pageable
			);
		}

		if (hasMember && hasDate) {
			return taskRepository.findByMemberAndCreatedAtBetween(
				member,
				start,
				end,
				pageable
			);
		}

		if (hasMember) {
			return taskRepository.findByMember(member, pageable);
		}

		if (hasTaskType) {
			return taskRepository.findByTaskType(taskType, pageable);
		}

		if (hasDate) {
			return taskRepository.findByCreatedAtBetween(start, end, pageable);
		}

		return taskRepository.findAll(pageable);
	}
}
