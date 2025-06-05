package kr.co.jeelee.kiwee.domain.platform.dto.response;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.platform.entity.Platform;

public record PlatformDetailResponse(
	UUID id, String name, String icon,
	String banner, String description, String page
) {
	public static PlatformDetailResponse from(Platform platform) {
		return new PlatformDetailResponse(
			platform.getId(),
			platform.getName(),
			platform.getIcon(),
			platform.getBanner(),
			platform.getDescription(),
			platform.getPage()
		);
	}
}
