package kr.co.jeelee.kiwee.domain.pledge.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.pledge.dto.request.PledgeCreateRequest;
import kr.co.jeelee.kiwee.domain.pledge.dto.response.PledgeDetailResponse;
import kr.co.jeelee.kiwee.domain.pledge.dto.response.PledgeSimpleResponse;
import kr.co.jeelee.kiwee.domain.pledge.service.PledgeService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.model.TermType;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/pledges")
@RequiredArgsConstructor
@Validated
public class PledgeController {

	private final PledgeService pledgeService;

	@PostMapping
	public PledgeDetailResponse createPledge(
		@AuthenticationPrincipal CustomOAuth2User principal,
		PledgeCreateRequest pledgeCreateRequest
	) {
		return pledgeService.createPledge(principal.member().getId(), pledgeCreateRequest);
	}

	@GetMapping(value = "/{id}")
	public PledgeDetailResponse getPledgeDetail(
		@PathVariable UUID id
	) {
		return pledgeService.getPledgeDetail(id);
	}

	@GetMapping
	public PagedResponse<PledgeSimpleResponse> getPledges(
		@RequestParam UUID proposerId,
		@RequestParam TermType termType,
		@PageableDefault Pageable pageable
	) {
		return pledgeService.getPledges(proposerId, termType, pageable);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deletePledge(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID id
	) {
		pledgeService.deletePledge(principal.member().getId(), id);
		return ResponseEntity.ok().build();
	}

}
