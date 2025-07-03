package kr.co.jeelee.kiwee.domain.memberActivity.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.memberActivity.dto.response.MemberActivityResponse;
import kr.co.jeelee.kiwee.domain.memberActivity.service.MemberActivityService;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/activities")
@RequiredArgsConstructor
@Validated
public class MemberActivityController {

	private final MemberActivityService memberActivityService;

	@GetMapping(value = "/me")
	public PagedResponse<MemberActivityResponse> myActivities(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PageableDefault Pageable pageable
	) {
		return memberActivityService.memberActivities(principal.member().getId(), pageable);
	}

	@PreAuthorize(value = "hasRole('OTHER_ACTVITY')")
	@GetMapping(value = "/{memberId}")
	public PagedResponse<MemberActivityResponse> memberActivities(
		@PathVariable UUID memberId,
		@PageableDefault Pageable pageable
	) {
		return memberActivityService.memberActivities(memberId, pageable);
	}

}
