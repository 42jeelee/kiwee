package kr.co.jeelee.kiwee.domain.channel.controller;

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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.channel.dto.request.ChannelCreateRequest;
import kr.co.jeelee.kiwee.domain.channel.dto.request.ChannelUpdateRequest;
import kr.co.jeelee.kiwee.domain.channel.dto.response.ChannelDetailResponse;
import kr.co.jeelee.kiwee.domain.channel.dto.response.ChannelSimpleResponse;
import kr.co.jeelee.kiwee.domain.channel.service.ChannelService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/channels")
@RequiredArgsConstructor
@Validated
public class ChannelController {

	private final ChannelService channelService;

	@PreAuthorize(value = "hasRole('CREATE_CHANNEL')")
	@PostMapping
	public ChannelDetailResponse createChannel(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@Valid @RequestBody ChannelCreateRequest request
	) {
		return channelService.createChannel(principal, request);
	}

	@PreAuthorize(value = "hasRole('READ_CHANNEL')")
	@GetMapping(value = "/all")
	public PagedResponse<ChannelSimpleResponse> getAllChannels(
		@PageableDefault Pageable pageable
	) {
		return channelService.getAllChannels(pageable);
	}

	@GetMapping
	public PagedResponse<ChannelSimpleResponse> getPublicChannels(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PageableDefault Pageable pageable
	) {
		return channelService.getAllPublicChannels(principal, pageable);
	}

	@GetMapping(value = "/{id}")
	public ChannelDetailResponse getChannelDetailById(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID id
	) {
		return channelService.getChannelDetailById(principal, id);
	}

	@PatchMapping(value = "/{id}")
	public ChannelDetailResponse updateChannel(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID id,
		@Valid @RequestBody ChannelUpdateRequest request
	) {
		return channelService.updateChannel(principal, id, request);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteChannel(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID id
	) {
		channelService.deleteChannel(principal, id);
		return ResponseEntity.noContent().build();
	}

}
