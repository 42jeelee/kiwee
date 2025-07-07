package kr.co.jeelee.kiwee.domain.badge.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import kr.co.jeelee.kiwee.domain.badge.dto.request.BadgeCreateRequest;
import kr.co.jeelee.kiwee.domain.badge.dto.request.BadgeLevelCreateRequest;
import kr.co.jeelee.kiwee.domain.badge.dto.request.BadgeLevelUpdateRequest;
import kr.co.jeelee.kiwee.domain.badge.dto.request.BadgeUpdateRequest;
import kr.co.jeelee.kiwee.domain.badge.dto.response.BadgeDetailResponse;
import kr.co.jeelee.kiwee.domain.badge.dto.response.BadgeLevelResponse;
import kr.co.jeelee.kiwee.domain.badge.dto.response.BadgeSimpleResponse;
import kr.co.jeelee.kiwee.domain.badge.service.BadgeLevelService;
import kr.co.jeelee.kiwee.domain.badge.service.BadgeService;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/badges")
@RequiredArgsConstructor
@Validated
public class BadgeController {

	private final BadgeService badgeService;
	private final BadgeLevelService badgeLevelService;

	@PreAuthorize(value = "hasRole('CREATE_BADGE')")
	@PostMapping
	public BadgeDetailResponse createBadge(
		@Valid @RequestBody BadgeCreateRequest request
	) {
		return  badgeService.createBadge(request);
	}

	@GetMapping(value = "/{id}")
	public BadgeDetailResponse badgeDetail(
		@PathVariable UUID id
	) {
		return badgeService.badgeDetail(id);
	}

	@GetMapping
	public PagedResponse<BadgeSimpleResponse> allPublicBadges(
		@RequestParam(defaultValue = "1") int level,
		@PageableDefault Pageable pageable
	) {
		return badgeService.allPublicBadges(level, pageable);
	}

	@PreAuthorize(value = "hasRole('EDIT_BADGE')")
	@PatchMapping(value = "/{id}")
	public BadgeDetailResponse updateBadge(
		@PathVariable UUID id,
		@Valid @RequestBody BadgeUpdateRequest request
	) {
		return badgeService.updateBadge(id, request);
	}

	@PreAuthorize(value = "hasRole('DELETE_BADGE')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteBadge(
		@PathVariable UUID id
	) {
		badgeService.deleteBadge(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize(value = "hasRole('CREATE_BADGE')")
	@PostMapping(value = "/{id}/level")
	public BadgeLevelResponse addLevelByBadgeId(
		@PathVariable UUID id,
		@Valid @RequestBody BadgeLevelCreateRequest request
	) {
		return badgeLevelService.addLevel(id, request);
	}

	@PreAuthorize(value = "hasRole('EDIT_BADGE')")
	@PatchMapping(value = "/{id}/levels/{levelId}")
	public BadgeLevelResponse updateLevel(
		@PathVariable UUID id,
		@PathVariable Long levelId,
		@Valid @RequestBody BadgeLevelUpdateRequest request
	) {
		return  badgeLevelService.updateLevel(id, levelId, request);
	}

	@PreAuthorize(value = "hasRole('DELETE_BADGE')")
	@DeleteMapping(value = "/{id}/levels/{levelId}")
	public ResponseEntity<Void> deleteLevel(
		@PathVariable UUID id,
		@PathVariable Long levelId
	) {
		badgeLevelService.deleteLevel(id, levelId);
		return ResponseEntity.noContent().build();
	}

}
