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
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.channel.service.ChannelService;
import kr.co.jeelee.kiwee.domain.channelMember.service.ChannelMemberService;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.memberActivity.event.MemberActivityEvent;
import kr.co.jeelee.kiwee.global.model.ActivityType;
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
	private final ChannelService channelService;
	private final ChannelMemberService channelMemberService;

	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public TaskResponse createTask(UUID channelId, UUID memberId, TaskCreateRequest request) {
		validateHasCreateAuthority(memberId);
		Task task = taskCreatorDispatcher.create(channelId, memberId, request);
		Task savedTask = taskRepository.save(task);

		eventPublisher.publishEvent(MemberActivityEvent.of(
			memberId,
			DomainType.TASK,
			savedTask.getId(),
			ActivityType.RECORD
		));

		return TaskResponse.from(savedTask);
	}

	@Override
	public TaskResponse getTask(UUID id) {
		Task task = taskRepository.findById(id)
			.orElseThrow(TaskNotFoundException::new);

		validateJoinedChannel(task.getChannel());
		return TaskResponse.from(task);
	}

	@Override
	public PagedResponse<TaskResponse> getTasks(UUID channelId, UUID memberId, TaskType taskType, LocalDate date,
		Pageable pageable) {
		Channel channel = channelService.getById(channelId);
		Member member = memberId != null
			? memberService.getById(memberId)
			: null;

		validateJoinedChannel(channel);
		return PagedResponse.of(
			fetchTasks(channel, member, taskType, date, pageable),
			TaskResponse::from
		);
	}

	@Override
	public PagedResponse<TaskResponse> getReviewTasksByContentId(UUID channelId, UUID contentId, Pageable pageable) {
		Channel channel = channelService.getById(channelId);

		validateJoinedChannel(channel);
		return PagedResponse.of(
			taskRepository.findChannelReviewTaskByContentId(
				channel,
				TaskType.REVIEW,
				contentId.toString(),
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

	private void validateJoinedChannel(Channel channel) {
		Member member = SecurityUtil.getLoginMember();

		if (!channelMemberService.isJoined(channel, member)) {
			throw new AccessDeniedException("채널 회원만 볼 수 있습니다.");
		}
	}

	private Page<Task> fetchTasks(
		Channel channel,
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
			return taskRepository.findByChannelAndMemberAndTaskTypeAndCreatedAtBetween(
				channel,
				member,
				taskType,
				start,
				end,
				pageable
			);
		}

		if (hasMember && hasTaskType) {
			return taskRepository.findByChannelAndMemberAndTaskType(
				channel,
				member,
				taskType,
				pageable
			);
		}

		if (hasTaskType && hasDate) {
			return taskRepository.findByChannelAndTaskTypeAndCreatedAtBetween(
				channel,
				taskType,
				start,
				end,
				pageable
			);
		}

		if (hasMember && hasDate) {
			return taskRepository.findByChannelAndMemberAndCreatedAtBetween(
				channel,
				member,
				start,
				end,
				pageable
			);
		}

		if (hasMember) {
			return taskRepository.findByChannelAndMember(channel, member, pageable);
		}

		if (hasTaskType) {
			return taskRepository.findByChannelAndTaskType(channel, taskType, pageable);
		}

		if (hasDate) {
			return taskRepository.findByChannelAndCreatedAtBetween(channel, start, end, pageable);
		}

		return taskRepository.findByChannel(channel, pageable);
	}
}
