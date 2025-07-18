package kr.co.jeelee.kiwee.domain.notification.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.notification.dto.response.NotificationResponse;
import kr.co.jeelee.kiwee.domain.notification.service.NotificationService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/members")
@RequiredArgsConstructor
@Validated
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping(value = "/me/notifications")
	public PagedResponse<NotificationResponse> getMyNotifications(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@RequestParam(defaultValue = "false") boolean read,
		@PageableDefault Pageable pageable
	) {
		return read
			? notificationService.getNotifications(principal.member().getId(), pageable)
			: notificationService.getUnReadNotifications(principal.member().getId(), pageable);
	}

	@PatchMapping(value = "/me/notifications/{id}/read")
	public NotificationResponse readNotification(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID id,
		@RequestParam(defaultValue = "true") boolean read
	) {
		return notificationService.readNotification(principal.member().getId(), id, read);
	}

	@DeleteMapping(value = "/me/notifications/{id}")
	public ResponseEntity<Void> deleteNotification(
		@AuthenticationPrincipal CustomOAuth2User principal,
		@PathVariable UUID id
	) {
		notificationService.deleteNotification(principal.member().getId(), id);
		return ResponseEntity.noContent().build();
	}

}
