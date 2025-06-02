package kr.co.jeelee.kiwee.domain.reputations.controller;

import java.time.YearMonth;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.reputations.dto.request.ReputationsCreateRequest;
import kr.co.jeelee.kiwee.domain.reputations.dto.response.ReputationsStatResponse;
import kr.co.jeelee.kiwee.domain.reputations.dto.response.ReputationsVoteResponse;
import kr.co.jeelee.kiwee.domain.reputations.service.ReputationService;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/reputations")
@RequiredArgsConstructor
@Validated
public class ReputationsController {

	private final ReputationService reputationService;

	@PostMapping
	public ResponseEntity<Void> vote(
		@Valid @RequestBody ReputationsCreateRequest request
	) {
		reputationService.vote(request);

		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/ranking")
	public PagedResponse<ReputationsStatResponse> getLank(
		@RequestParam(required = false) YearMonth yearMonth,
		@PageableDefault Pageable pageable
	) {
		if (yearMonth == null) {
			yearMonth = YearMonth.now();
		}

		return reputationService.getLank(yearMonth, pageable);
	}

	@GetMapping(value = "/{id}")
	public ReputationsStatResponse getStatByMemberId(
		@PathVariable UUID id,
		@RequestParam(required = false) YearMonth yearMonth
	) {
		if (yearMonth == null) {
			yearMonth = YearMonth.now();
		}

		return reputationService.getStatByMemberId(id, yearMonth);
	}

	@GetMapping(value = "/{id}/logs")
	public PagedResponse<ReputationsVoteResponse> getVotesByMemberId(
		@PathVariable UUID id,
		@PageableDefault Pageable pageable
	) {
		return reputationService.getVotesByMemberId(id, pageable);
	}

}
