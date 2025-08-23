package kr.co.jeelee.kiwee.domain.memberTheme.controller;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.memberTheme.dto.request.MemberThemeRequest;
import kr.co.jeelee.kiwee.domain.memberTheme.dto.response.MemberThemeResponse;
import kr.co.jeelee.kiwee.domain.memberTheme.service.MemberThemeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/members/{memberId}/themes")
@RequiredArgsConstructor
@Validated
public class MemberThemeController {

	private final MemberThemeService memberThemeService;

	@PostMapping
	public MemberThemeResponse updateOrCreateTheme(
		@PathVariable UUID memberId,
		@Valid @RequestBody MemberThemeRequest memberThemeRequest
	) {
		return MemberThemeResponse.from(
			memberThemeService.updateOrCreateMemberTheme(memberId, memberThemeRequest.name())
		);
	}

	@GetMapping
	public MemberThemeResponse getTheme(
		@PathVariable UUID memberId
	) {
		return MemberThemeResponse.from(memberThemeService.getMemberTheme(memberId));
	}

}
