package kr.co.jeelee.kiwee.domain.task.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.task.dto.request.TaskCreateRequest;
import kr.co.jeelee.kiwee.domain.task.dto.response.TaskResponse;
import kr.co.jeelee.kiwee.domain.task.model.TaskType;
import kr.co.jeelee.kiwee.domain.task.service.TaskService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

	private final TaskService taskService;

	@PostMapping(value = "/channels/{channelId}/members/me")
	public TaskResponse createMyTask(
		@PathVariable UUID channelId,
		@AuthenticationPrincipal CustomOAuth2User principal,
		@Valid @RequestBody TaskCreateRequest request
	) {
		return taskService.createTask(channelId, principal.member().getId(), request);
	}

	@PreAuthorize(value = "hasRole('CREATE_OTHER_TASK')")
	@PostMapping(value = "/channels/{channelId}/members/{memberId}")
	public TaskResponse createTask(
		@PathVariable UUID channelId,
		@PathVariable UUID memberId,
		@Valid @RequestBody TaskCreateRequest request
	) {
		return taskService.createTask(channelId, memberId, request);
	}

	@GetMapping(value = "/{id}")
	public TaskResponse getTask(
		@PathVariable UUID id
	) {
		return taskService.getTask(id);
	}

	@GetMapping(value = "/channels/{channelId}")
	public PagedResponse<TaskResponse> getTasks(
		@PathVariable UUID channelId,
		@RequestParam(required = false) UUID memberId,
		@RequestParam(required = false) TaskType taskType,
		@RequestParam(required = false) LocalDate date,
		@PageableDefault Pageable pageable
	) {
		return taskService.getTasks(channelId, memberId, taskType, date, pageable);
	}

	@GetMapping(value = "/channels/{channelId}/contents/{contentId}")
	public PagedResponse<TaskResponse> getReviewTasksByContentId(
		@PathVariable UUID channelId,
		@PathVariable UUID contentId,
		@PageableDefault Pageable pageable
	) {
		return taskService.getReviewTasksByContentId(channelId, contentId, pageable);
	}

}
