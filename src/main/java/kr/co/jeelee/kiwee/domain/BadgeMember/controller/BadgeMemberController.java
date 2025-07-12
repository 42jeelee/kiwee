package kr.co.jeelee.kiwee.domain.BadgeMember.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.jeelee.kiwee.domain.BadgeMember.dto.response.BadgeMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.BadgeMember.dto.response.BadgeMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.BadgeMember.service.BadgeMemberService;
import kr.co.jeelee.kiwee.domain.badge.dto.response.BadgeSimpleResponse;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Validated
public class BadgeMemberController {

	private final BadgeMemberService badgeMemberService;

	@GetMapping(value = "/members/{memberId}/badges/{badgeId}")
	public BadgeMemberDetailResponse findById(
		@PathVariable UUID memberId,
		@PathVariable UUID badgeId
	) {
		return badgeMemberService.findById(memberId, badgeId);
	}

	@GetMapping(value = "/members/{memberId}/badges")
	public PagedResponse<BadgeSimpleResponse> getBadgesByMemberId(
		@PathVariable UUID memberId,
		@PageableDefault Pageable pageable
	) {
		return badgeMemberService.getBadgesByMemberId(memberId, pageable);
	}

	@GetMapping(value = "/badges/{badgeId}/members")
	public PagedResponse<BadgeMemberSimpleResponse> getMembersByBadgeId(
		@PathVariable UUID badgeId,
		@PageableDefault Pageable pageable
	) {
		return badgeMemberService.getMembersByBadgeId(badgeId, pageable);
	}

	@PreAuthorize(value = "hasRole('DELETE_BADGE')")
	@DeleteMapping(value = "/members/{memberId}/badges/{badgeId}")
	public ResponseEntity<Void> revokeBadge(
		@PathVariable UUID memberId,
		@PathVariable UUID badgeId
	) {
		badgeMemberService.revokeBadge(memberId, badgeId);
		return ResponseEntity.noContent().build();
	}

}
