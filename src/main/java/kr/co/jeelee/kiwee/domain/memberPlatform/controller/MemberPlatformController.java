package kr.co.jeelee.kiwee.domain.memberPlatform.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.jeelee.kiwee.domain.memberPlatform.dto.response.MemberPlatformResponse;
import kr.co.jeelee.kiwee.domain.memberPlatform.service.MemberPlatformService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Validated
public class MemberPlatformController {

	private final MemberPlatformService memberPlatformService;

	@GetMapping(value = "/member-platforms")
	public PagedResponse<MemberPlatformResponse> getMemberPlatforms(
		@RequestParam(required = false) UUID memberId,
		@RequestParam(required = false) UUID platformId,
		@PageableDefault Pageable pageable
	) {
		return  memberPlatformService.getMemberPlatforms(memberId, platformId, pageable);
	}

}
