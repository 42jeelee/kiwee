package kr.co.jeelee.kiwee.domain.rewardMember.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.rewardMember.dto.response.RewardMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.rewardMember.dto.response.RewardMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.rewardMember.service.RewardMemberService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/members/me/rewards")
@RequiredArgsConstructor
@Validated
public class RewardMemberController {

	private final RewardMemberService rewardMemberService;

	@GetMapping
	public PagedResponse<RewardMemberSimpleResponse> getRewardsByMember(
		@AuthenticationPrincipal CustomOAuth2User principal,
		Pageable pageable
	) {
		return rewardMemberService.getRewardsByMember(principal.member().getId(), pageable);
	}

	@GetMapping(value = "/{id}")
	public RewardMemberDetailResponse getRewardDetail(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable Long id
	) {
		return rewardMemberService.getRewardMemberById(principal.member().getId(), id);
	}

	@PreAuthorize(value = "hasRole('DELETE_REWARD')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteRewardMember(
		@PathVariable Long id
	) {
		rewardMemberService.deleteRewardMemberById(id);

		return ResponseEntity.noContent().build();
	}

}
