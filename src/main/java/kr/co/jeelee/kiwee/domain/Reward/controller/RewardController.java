package kr.co.jeelee.kiwee.domain.Reward.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.Reward.dto.request.RewardCreateRequest;
import kr.co.jeelee.kiwee.domain.Reward.dto.response.RewardDetailResponse;
import kr.co.jeelee.kiwee.domain.Reward.dto.response.RewardSimpleResponse;
import kr.co.jeelee.kiwee.domain.Reward.service.RewardService;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Validated
public class RewardController {

	private final RewardService rewardService;

	@PreAuthorize(value = "hasRole('CREATE_REWARD')")
	@PostMapping(value = "/rewards")
	public RewardDetailResponse createReward(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@Valid @RequestBody RewardCreateRequest rewardCreateRequest
	) {
		return rewardService.createReward(principal, rewardCreateRequest);
	}

	@GetMapping(value = "/rewards")
	public PagedResponse<RewardSimpleResponse> publicRewards(
		@PageableDefault Pageable pageable
	) {
		return rewardService.getPublicRewards(pageable);
	}

	@GetMapping(value = "/{domainType}/{domainId}/rewards")
	public PagedResponse<RewardSimpleResponse> rewardsBySource(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable DomainType domainType,
		@PathVariable UUID domainId,
		@PageableDefault Pageable pageable
	) {
		return rewardService.getRewardsBySource(principal, domainType, domainId, pageable);
	}

	@PreAuthorize(value = "hasRole('DELETE_REWARD')")
	@DeleteMapping(value = "/rewards/{id}")
	public ResponseEntity<Void> deleteReward(
		@PathVariable UUID id
	) {
		rewardService.deleteReward(id);

		return ResponseEntity.noContent().build();
	}

}
