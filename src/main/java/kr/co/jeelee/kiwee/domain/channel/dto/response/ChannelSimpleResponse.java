package kr.co.jeelee.kiwee.domain.channel.dto.response;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.channel.entity.Channel;

public record ChannelSimpleResponse(
	UUID id, String name, String icon,
	String banner, Boolean isPublic, Boolean isJoined
) {
	public static ChannelSimpleResponse from(Channel channel) {
		return new ChannelSimpleResponse(
			channel.getId(),
			channel.getName(),
			channel.getIcon(),
			channel.getBanner(),
			channel.getIsPublic(),
			null
		);
	}
}
