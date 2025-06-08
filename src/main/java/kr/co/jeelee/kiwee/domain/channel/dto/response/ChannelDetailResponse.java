package kr.co.jeelee.kiwee.domain.channel.dto.response;

import java.util.List;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.platform.dto.response.PlatformDetailResponse;

public record ChannelDetailResponse(
	UUID id, String name, String icon,
	String banner, String description,
	List<PlatformDetailResponse> platform, Boolean isPublic
) {
	public static ChannelDetailResponse from(Channel channel) {

		List<PlatformDetailResponse> platforms = channel.getPlatformChannels().stream()
			.map(pc -> PlatformDetailResponse.from(pc.getPlatform()))
			.toList();

		return new ChannelDetailResponse(
			channel.getId(),
			channel.getName(),
			channel.getIcon(),
			channel.getBanner(),
			channel.getDescription(),
			platforms,
			channel.getIsPublic()
		);
	}
}
