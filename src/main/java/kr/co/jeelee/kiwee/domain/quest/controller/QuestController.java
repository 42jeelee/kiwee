package kr.co.jeelee.kiwee.domain.quest.controller;

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
import kr.co.jeelee.kiwee.domain.quest.dto.request.QuestCreateRequest;
import kr.co.jeelee.kiwee.domain.quest.dto.request.QuestUpdateRequest;
import kr.co.jeelee.kiwee.domain.quest.dto.response.QuestDetailResponse;
import kr.co.jeelee.kiwee.domain.quest.dto.response.QuestSimpleResponse;
import kr.co.jeelee.kiwee.domain.quest.service.QuestService;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/quests")
@RequiredArgsConstructor
@Validated
public class QuestController {

	private final QuestService questService;

	@PreAuthorize(value = "hasRole('CREATE_QUEST')")
	@PostMapping
	public QuestDetailResponse createQuest(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@Valid @RequestBody QuestCreateRequest request
	) {
		return questService.createQuest(principal, request);
	}

	@GetMapping(value = "/all/channels/{channelId}")
	public PagedResponse<QuestSimpleResponse> getQuestsByChannel(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID channelId,
		@PageableDefault Pageable pageable
	) {
		return questService.getAllQuestsByChannel(principal, channelId, pageable);
	}

	@GetMapping(value = "/{id}")
	public QuestDetailResponse questDetail(
		@PathVariable UUID id
	) {
		return questService.getQuestDetail(id);
	}

	@PreAuthorize(value = "hasRole('EDIT_QUEST')")
	@PatchMapping(value = "/{id}")
	public QuestDetailResponse updateQuest(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID id,
		@Valid @RequestBody QuestUpdateRequest request
	) {
		return questService.updateQuest(principal, id, request);
	}

	@PreAuthorize(value = "hasRole('EDIT_QUEST')")
	@PatchMapping(value = "/{id}/activate")
	public ResponseEntity<Void> switchActivate(
		@PathVariable UUID id,
		@RequestParam Boolean active
	) {
		if (active == null) {
			active = false;
		}

		questService.switchActivate(id, active);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteQuest(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID id
	) {
		questService.deleteQuest(principal, id);

		return ResponseEntity.noContent().build();
	}

	@PreAuthorize(value = "hasRole('DELETE_QUEST')")
	@DeleteMapping(value = "/{id}/force")
	public ResponseEntity<Void> deleteQuestForce(
		@PathVariable UUID id
	) {
		questService.deleteQuestForce(id);

		return ResponseEntity.noContent().build();
	}

}
