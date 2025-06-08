package kr.co.jeelee.kiwee.domain.channel.service;

import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;

public interface PlatformChannelService {

	void addPlatformChannel(Platform platform, Channel channel, String platformChannelId);

	void deletePlatformChannel(Platform platform, String platformChannelId);

}
