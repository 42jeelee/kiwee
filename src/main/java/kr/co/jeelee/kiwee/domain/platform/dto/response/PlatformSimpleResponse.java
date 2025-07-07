package kr.co.jeelee.kiwee.domain.platform.dto.response;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.platform.entity.Platform;

public record PlatformSimpleResponse(
	UUID id, String name, String icon, String banner
) {
	public static PlatformSimpleResponse from(Platform platform) {
		return new PlatformSimpleResponse(
			platform.getId(), platform.getName(), platform.getIcon(), platform.getBanner()
		);
	}
}
