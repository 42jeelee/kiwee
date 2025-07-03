package kr.co.jeelee.kiwee.domain.questMember.controller;

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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.questMember.dto.request.QuestMemberCreateRequest;
import kr.co.jeelee.kiwee.domain.questMember.dto.request.QuestMemberVerificationRequest;
import kr.co.jeelee.kiwee.domain.questMember.dto.response.QuestMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.questMember.dto.response.QuestMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.questMember.dto.response.QuestMemberVerificationResponse;
import kr.co.jeelee.kiwee.domain.questMember.service.QuestMemberService;
import kr.co.jeelee.kiwee.domain.questMember.service.QuestMemberVerificationService;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Validated
public class QuestMemberController {

	private final QuestMemberService questMemberService;
	private final QuestMemberVerificationService questMemberVerificationService;

	@PostMapping(value = "/me/quests/{questId}")
	public QuestMemberDetailResponse join(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID questId,
		@Valid @RequestBody QuestMemberCreateRequest request
	) {
		return questMemberService.joinQuest(principal, questId, request);
	}

	@GetMapping(value = "/me/quests/{questId}")
	public QuestMemberDetailResponse joinedQuest(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID questId
	) {
		return questMemberService.getQuestMember(principal, questId);
	}

	@GetMapping(value = "/me/quests")
	public PagedResponse<QuestMemberSimpleResponse> joinedQuests(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PageableDefault Pageable pageable
	) {
		return questMemberService.getQuestMembers(principal, pageable);
	}

	@GetMapping(value = "/quests/{questId}/members")
	public PagedResponse<QuestMemberSimpleResponse> joinedMembersByQuestId(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID questId,
		@PageableDefault Pageable pageable
	) {
		return questMemberService.findMembersByQuestId(principal, questId, pageable);
	}

	@DeleteMapping(value = "/me/quests/{questId}")
	public ResponseEntity<Void> giveUp(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID questId
	) {
		questMemberService.giveUpQuest(principal, questId);

		return ResponseEntity.noContent().build();
	}

	@PostMapping(value = "/me/quests/{questId}/verify")
	public QuestMemberVerificationResponse verify(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID questId,
		@Valid @RequestBody QuestMemberVerificationRequest request
	) {
		return questMemberVerificationService.verify(principal, questId, request);
	}

	@GetMapping(value = "/me/quests/verifications")
	public PagedResponse<QuestMemberVerificationResponse> myVerifications(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PageableDefault Pageable pageable
	) {
		return questMemberVerificationService.getAllByMember(principal, pageable);
	}

	@GetMapping(value = "/quests/{questId}/verifications")
	public PagedResponse<QuestMemberVerificationResponse> verificationsByQuest(
		@PathVariable UUID questId,
		@PageableDefault Pageable pageable
	) {
		return questMemberVerificationService.getAllByQuest(questId, pageable);
	}

}
