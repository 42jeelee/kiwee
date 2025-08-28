package kr.co.jeelee.kiwee.domain.content.dto.response;

import kr.co.jeelee.kiwee.domain.content.entity.PlatformContent;
import kr.co.jeelee.kiwee.domain.platform.dto.response.PlatformSimpleResponse;

public record PlatformContentResponse(
	PlatformSimpleResponse platform, ContentSimpleResponse content, String idInPlatform
) {
	public static PlatformContentResponse from(PlatformContent platformContent) {
		return new PlatformContentResponse(
			PlatformSimpleResponse.from(platformContent.getPlatform()),
			ContentSimpleResponse.from(platformContent.getContent()),
			platformContent.getIdInPlatform()
		);
	}
}
