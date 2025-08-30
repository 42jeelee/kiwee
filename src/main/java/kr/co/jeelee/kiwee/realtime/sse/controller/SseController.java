package kr.co.jeelee.kiwee.realtime.sse.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.realtime.sse.service.SseService;
import kr.co.jeelee.kiwee.realtime.sse.service.StreamTokenService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/sse")
@RequiredArgsConstructor
public class SseController {

	private final SseService sseService;
	private final StreamTokenService streamTokenService;

	@PostMapping("/token")
	public Map<String, String> issue(@AuthenticationPrincipal CustomOAuth2User principal) {
		return Map.of("token", streamTokenService.issue(principal.member().getId()));
	}

	@GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter stream(
		@RequestParam(required = false) String token,
		@AuthenticationPrincipal CustomOAuth2User principal,
		@RequestHeader(name = "Last-Event-ID", required = false) String lastEventId
	) {
		UUID memberId = principal != null
			? principal.member().getId()
			: streamTokenService.validateAndConsume(token);

		return sseService.subscribe(memberId);
	}

}
