package kr.co.jeelee.kiwee.domain.member.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import kr.co.jeelee.kiwee.domain.member.dto.request.GainExpRequest;
import kr.co.jeelee.kiwee.domain.member.dto.request.MemberCreateRequest;
import kr.co.jeelee.kiwee.domain.member.dto.request.UpdateMemberRequest;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberDetailResponse;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/members")
@RequiredArgsConstructor
@Validated
public class MemberController {

	private final MemberService memberService;

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

	@PatchMapping(value = "/{id}")
	public MemberDetailResponse changeEmail(
		@PathVariable UUID id,
		@Valid @RequestBody UpdateMemberRequest request
	) {
		return memberService.updateMember(id, request);
	}

	@PatchMapping(value = "/{id}/exp")
	public MemberDetailResponse gainExp(
		@PathVariable UUID id,
		@Valid @RequestBody GainExpRequest request
	) {
		return memberService.gainExp(id, request);
	}

	@DeleteMapping(value = "/{id}")
	public void deleteMember(
		@PathVariable UUID id
	) {
		memberService.deleteMemberById(id);
	}

}
