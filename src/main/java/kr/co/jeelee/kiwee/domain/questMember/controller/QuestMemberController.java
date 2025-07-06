package kr.co.jeelee.kiwee.domain.questMember.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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
import kr.co.jeelee.kiwee.domain.questMember.dto.request.QuestMemberPlannedRequest;
import kr.co.jeelee.kiwee.domain.questMember.dto.request.QuestMemberSuccessRequest;
import kr.co.jeelee.kiwee.domain.questMember.dto.response.QuestMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.questMember.dto.response.QuestMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberStatus;
import kr.co.jeelee.kiwee.domain.questMember.service.QuestMemberService;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Validated
public class QuestMemberController {

	private final QuestMemberService questMemberService;

	@PostMapping(value = "quests/{questId}/members")
	public QuestMemberDetailResponse successQuest(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID questId,
		@Valid @RequestBody QuestMemberSuccessRequest request
	) {
		return questMemberService.successQuest(principal, questId, request);
	}

	@PostMapping(value = "quests/{questId}/members/me/plan")
	public QuestMemberDetailResponse plannedQuest(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID questId,
		@Valid @RequestBody QuestMemberPlannedRequest request
	) {
		return questMemberService.plannedQuest(principal, questId, request);
	}

	@GetMapping(value = "/members/me/quests/{questId}")
	public QuestMemberDetailResponse joinedQuest(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID questId
	) {
		return questMemberService.getQuestMember(principal, questId);
	}

	@GetMapping(value = "/members/me/quests")
	public PagedResponse<QuestMemberSimpleResponse> joinedQuests(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@RequestParam(defaultValue = "IN_PROGRESS") QuestMemberStatus status,
		@PageableDefault Pageable pageable
	) {
		return questMemberService.getQuestMembers(principal, status, pageable);
	}

	@GetMapping(value = "/quests/{questId}/members")
	public PagedResponse<QuestMemberSimpleResponse> joinedMembersByQuestId(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID questId,
		@PageableDefault Pageable pageable
	) {
		return questMemberService.findMembersByQuestId(principal, questId, pageable);
	}

	@PatchMapping(value = "/members/me/quests/{questId}/{id}")
	public QuestMemberDetailResponse updatePlannedQuest(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID questId,
		@PathVariable UUID id,
		@Valid @RequestBody QuestMemberPlannedRequest request
	) {
		return questMemberService.updatePlannedQuest(principal, questId, id, request);
	}

	@PatchMapping(value = "/members/me/quests/{questId}/give-up")
	public ResponseEntity<Void> giveUp(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID questId
	) {
		questMemberService.giveUpQuest(principal, questId);

		return ResponseEntity.noContent().build();
	}

}
