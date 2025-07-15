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

	@PostMapping(value = "/members/me")
	public TaskResponse createMyTask(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@Valid @RequestBody TaskCreateRequest request
	) {
		return taskService.createTask(principal.member().getId(), request);
	}

	@PreAuthorize(value = "hasRole('CREATE_OTHER_TASK')")
	@PostMapping(value = "/members/{memberId}")
	public TaskResponse createTask(
		@PathVariable UUID memberId,
		@Valid @RequestBody TaskCreateRequest request
	) {
		return taskService.createTask(memberId, request);
	}

	@GetMapping(value = "/{id}")
	public TaskResponse getTask(
		@PathVariable UUID id
	) {
		return taskService.getTask(id);
	}

	@GetMapping
	public PagedResponse<TaskResponse> getTasks(
		@RequestParam(required = false) UUID memberId,
		@RequestParam(required = false) TaskType taskType,
		@RequestParam(required = false) LocalDate date,
		@PageableDefault Pageable pageable
	) {
		return taskService.getTasks(memberId, taskType, date, pageable);
	}

	@GetMapping(value = "/contents/{applicationId}")
	public PagedResponse<TaskResponse> getPlayTasksByApplicationId(
		@PathVariable String applicationId,
		@PageableDefault Pageable pageable
	) {
		return taskService.getPlayTasksByApplicationId(applicationId, pageable);
	}

}
