package kr.co.jeelee.kiwee.domain.channelMember.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.authorization.dto.request.RolesRequest;
import kr.co.jeelee.kiwee.domain.authorization.model.RoleType;
import kr.co.jeelee.kiwee.domain.channelMember.dto.response.ChannelMemberResponse;
import kr.co.jeelee.kiwee.domain.channelMember.service.ChannelMemberService;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/channels/{channelId}")
@RequiredArgsConstructor
@Validated
public class ChannelMemberController {

	private final ChannelMemberService channelMemberService;

	@PostMapping(value = "/join")
	public ChannelMemberResponse joinPublicChannel(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID channelId
	){
		return channelMemberService.joinPublicChannel(principal, channelId);
	}

	@GetMapping(value = "/members")
	public PagedResponse<ChannelMemberResponse> getChannelMembers(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID channelId,
		@PageableDefault Pageable pageable
	) {
		return channelMemberService.findMembersByChannel(principal, channelId, pageable);
	}

	@PostMapping(value = "/members/{memberId}/roles")
	public ChannelMemberResponse addRoles(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID channelId,
		@PathVariable UUID memberId,
		@Valid @RequestBody RolesRequest request
	) {
		return channelMemberService.addRolesAtChannel(principal, channelId, memberId, request);
	}

	@DeleteMapping(value = "/members/{memberId}/roles/{roleType}")
	public ChannelMemberResponse removeRoles(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID channelId,
		@PathVariable UUID memberId,

		@PathVariable RoleType roleType
	) {
		return channelMemberService.removeRoleAtChannel(principal, channelId, memberId, roleType);
	}

	@PostMapping(value = "/members/{memberId}/ben")
	public ChannelMemberResponse channelMemberBenOrUnBen(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID channelId,
		@PathVariable UUID memberId,
		@RequestParam Boolean isBen
	) {
		if (isBen == null) {
			isBen = true;
		}
		return channelMemberService.updateBen(principal, channelId, memberId, isBen);
	}

	@DeleteMapping(value = "/members/{memberId}")
	public ResponseEntity<Void> kickMember(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID channelId,
		@PathVariable UUID memberId
	) {
		channelMemberService.kickChannelMember(principal, channelId, memberId);
		return ResponseEntity.noContent().build();
	}

}
