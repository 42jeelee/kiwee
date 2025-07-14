package kr.co.jeelee.kiwee.domain.pledgeMember.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.pledgeMember.dto.request.PledgeMemberCreateRequest;
import kr.co.jeelee.kiwee.domain.pledgeMember.dto.response.PledgeMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.pledgeMember.dto.response.PledgeMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.pledgeMember.service.PledgeMemberService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/qpi/v1")
@RequiredArgsConstructor
@Validated
public class PledgeMemberController {

	private final PledgeMemberService pledgeMemberService;

	@PostMapping(value = "/pledges/{pledgeId}/members/{memberId}")
	public PledgeMemberDetailResponse joinPledge(
		@PathVariable UUID pledgeId,
		@PathVariable UUID memberId,
		@Valid @RequestBody PledgeMemberCreateRequest pledgeMemberCreateRequest
	) {
		return pledgeMemberService.joinPledge(memberId, pledgeId, pledgeMemberCreateRequest);
	}

	@GetMapping(value = "/pledge-members/{id}")
	public PledgeMemberDetailResponse getPledge(
		@PathVariable UUID id
	) {
		return pledgeMemberService.getPledgeMember(id);
	}

	@GetMapping(value = "/pledge-members")
	public PagedResponse<PledgeMemberSimpleResponse> getMyPledges(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PageableDefault Pageable pageable
	) {
		return pledgeMemberService.getMemberPledges(principal.member().getId(), pageable);
	}

	@GetMapping(value = "/pledges/{pledgeId}/members")
	public PagedResponse<PledgeMemberSimpleResponse> getPledgeMembers(
		@PathVariable UUID pledgeId,
		@PageableDefault Pageable pageable
	) {
		return pledgeMemberService.getPledgeMembers(pledgeId, pageable);
	}

	@PatchMapping(value = "/pledge-members/{id}/delay")
	public PledgeMemberSimpleResponse delayPledge(
		@PathVariable UUID id,
		@RequestBody LocalDateTime startAt
	) {
		return pledgeMemberService.delayPledge(id, startAt);
	}

	@PatchMapping(value = "/pledge-members/{id}/give-up")
	public PledgeMemberSimpleResponse giveUpPledge(
		@PathVariable UUID id
	) {
		return pledgeMemberService.giveUpPledge(id);
	}

}
