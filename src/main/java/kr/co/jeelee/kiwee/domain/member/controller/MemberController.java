package kr.co.jeelee.kiwee.domain.member.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.member.dto.request.GainExpRequest;
import kr.co.jeelee.kiwee.domain.member.dto.request.MemberCreateRequest;
import kr.co.jeelee.kiwee.domain.member.dto.request.MemberRolesRequest;
import kr.co.jeelee.kiwee.domain.member.dto.request.UpdateMemberRequest;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberDetailResponse;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberRolesResponse;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.InvalidParameterException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/members")
@RequiredArgsConstructor
@Validated
public class MemberController {

	private final MemberService memberService;

	@PreAuthorize(value = "hasRole('CREATE_MEMBER')")
	@PostMapping
	public MemberDetailResponse createMember(
		@Valid @RequestBody MemberCreateRequest memberCreateRequest
	) {
		return memberService.createMember(memberCreateRequest);
	}

	@GetMapping
	public PagedResponse<MemberSimpleResponse> getMembers(
		@RequestParam(required = false) String keyword,
		@PageableDefault Pageable pageable
	) {
		return keyword == null || keyword.isBlank()
			? memberService.getAllMembers(pageable)
			: memberService.searchMembers(keyword, pageable);
	}

	@GetMapping(value = "/{id}")
	public MemberDetailResponse getMemberById(
		@PathVariable UUID id
	) {
		return memberService.getMemberById(id);
	}

	@PreAuthorize(value = "hasRole('PERMISSION_GRANTER')")
	@GetMapping(value = "/{id}/roles")
	public MemberRolesResponse getMemberRoles(
		@PathVariable UUID id
	) {
		return memberService.getMemberRoles(id);
	}

	@PreAuthorize(value = "hasRole('PERMISSION_GRANTER')")
	@PostMapping(value = "/{id}/roles")
	public MemberRolesResponse addMemberRoles(
		@PathVariable UUID id,
		@Valid @RequestBody MemberRolesRequest request
	) {
		return memberService.addRoles(id, request);
	}

	@PreAuthorize(value = "hasRole('EDIT_MEMBER')")
	@PatchMapping(value = "/{id}")
	public MemberDetailResponse changeEmail(
		@PathVariable UUID id,
		@Valid @RequestBody UpdateMemberRequest request
	) {
		return memberService.updateMember(id, request);
	}

	@PreAuthorize(value = "hasRole('EDIT_MEMBER')")
	@PatchMapping(value = "/{id}/exp")
	public MemberDetailResponse gainExp(
		@PathVariable UUID id,
		@Valid @RequestBody GainExpRequest request
	) {
		return memberService.gainExp(id, request);
	}

	@PreAuthorize(value = "hasRole('PERMISSION_GRANTER')")
	@DeleteMapping(value = "/{id}/roles/{roleName}")
	public ResponseEntity<Void> deleteMemberRoles(
		@PathVariable UUID id,
		@PathVariable String roleName
	) {
		if (roleName == null || roleName.trim().isBlank()) {
			throw new InvalidParameterException("role");
		}
		memberService.deleteRole(id, roleName);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize(value = "hasRole('DELETE_MEMBER')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteMember(
		@PathVariable UUID id
	) {
		memberService.deleteMemberById(id);

		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/me")
	public MemberDetailResponse getMe(@AuthenticationPrincipal CustomOAuth2User principal) {
		return MemberDetailResponse.from(principal.member());
	}

	@PatchMapping(value = "/me")
	public MemberDetailResponse updateMe(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@Valid @RequestBody UpdateMemberRequest request
	) {
		return memberService.updateMember(principal.member().getId(), request);
	}

	@DeleteMapping(value = "/me")
	public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal CustomOAuth2User principal) {
		memberService.deleteMemberById(principal.member().getId());
		return ResponseEntity.noContent().build();
	}

}
