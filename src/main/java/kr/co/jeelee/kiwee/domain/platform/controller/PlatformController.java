package kr.co.jeelee.kiwee.domain.platform.controller;

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
import kr.co.jeelee.kiwee.domain.platform.dto.request.PlatformCreateRequest;
import kr.co.jeelee.kiwee.domain.platform.dto.request.PlatformUpdateRequest;
import kr.co.jeelee.kiwee.domain.platform.dto.response.PlatformDetailResponse;
import kr.co.jeelee.kiwee.domain.platform.service.PlatformService;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/platforms")
@RequiredArgsConstructor
@Validated
public class PlatformController {

	private final PlatformService platformService;

	@PreAuthorize(value = "hasRole('CREATE_PLATFORM')")
	@PostMapping
	public PlatformDetailResponse createPlatform(
		@Valid @RequestBody PlatformCreateRequest request
	) {
		return platformService.createPlatform(request);
	}

	@GetMapping
	public PagedResponse<PlatformDetailResponse> getPlatforms(
		@RequestParam(required = false) String keyword,
		@PageableDefault Pageable pageable
	) {
		return keyword == null || keyword.isBlank()
			? platformService.getAllPlatforms(pageable)
			: platformService.searchPlatforms(keyword, pageable);
	}

	@GetMapping(value = "/{id}")
	public PlatformDetailResponse getPlatformById(
		@PathVariable UUID id
	) {
		return platformService.getPlatformById(id);
	}

	@PreAuthorize(value = "hasRole('EDIT_PLATFORM')")
	@PatchMapping(value = "/{id}")
	public PlatformDetailResponse updatePlatform(
		@PathVariable UUID id,
		@Valid @RequestBody PlatformUpdateRequest request
	) {
		return platformService.updatePlatform(id, request);
	}

	@PreAuthorize(value = "hasRole('DELETE_PLATFORM')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deletePlatform(
		@PathVariable UUID id
	) {
		platformService.deletePlatformById(id);

		return ResponseEntity.noContent().build();
	}

}
