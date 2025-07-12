package kr.co.jeelee.kiwee.domain.invite.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.invite.dto.request.InviteCreateRequest;
import kr.co.jeelee.kiwee.domain.invite.dto.response.InviteDetailResponse;
import kr.co.jeelee.kiwee.domain.invite.dto.response.InviteSimpleResponse;
import kr.co.jeelee.kiwee.domain.invite.service.InviteService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/invites")
@RequiredArgsConstructor
@Validated
public class InviteController {

	private final InviteService inviteService;

	@PostMapping
	public InviteDetailResponse inviteMember(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@Valid @RequestBody InviteCreateRequest request
	) {
		return inviteService.invite(principal, request);
	}

	@GetMapping
	public PagedResponse<InviteSimpleResponse> getAllPublicInvite(
		@PageableDefault Pageable pageable
	) {
		return inviteService.getAllPublicInvite(pageable);
	}

	@GetMapping(value = "/me")
	public PagedResponse<InviteSimpleResponse> getReceiveInvite(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PageableDefault Pageable pageable
	) {
		return inviteService.getReceiveMe(principal, pageable);
	}

	@GetMapping(value = "/{id}")
	public InviteDetailResponse getById(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID id
	) {
		return inviteService.getById(principal, id);
	}

	@GetMapping(value = "/code/{code}")
	public InviteDetailResponse getByCode(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable String code
	) {
		return inviteService.getByCode(principal, code);
	}

	@PostMapping(value = "/{code}/accept")
	public ResponseEntity<Void> acceptInvite(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable String code
	) {
		inviteService.accept(principal, code);
		return ResponseEntity.noContent().build();
	}

	@PostMapping(value = "/{code}/reject")
	public ResponseEntity<Void> rejectInvite(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable String code
	) {
		inviteService.reject(principal, code);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize(value = "hasRole('INVITE_EXPIRED')")
	@PostMapping(value = "/{code}/expired/force")
	public ResponseEntity<Void> expireInvite(
		@PathVariable String code
	) {
		inviteService.expired(code);
		return ResponseEntity.noContent().build();
	}

	@PostMapping(value = "/{code}/expired")
	public ResponseEntity<Void> expiredMyInvite(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable String code
	) {
		inviteService.expiredMyInvite(principal, code);
		return ResponseEntity.noContent().build();
	}

}
