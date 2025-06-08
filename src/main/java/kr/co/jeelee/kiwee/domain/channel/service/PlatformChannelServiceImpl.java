package kr.co.jeelee.kiwee.domain.channel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.channel.entity.PlatformChannel;
import kr.co.jeelee.kiwee.domain.channel.exception.ChannelAlreadyExistException;
import kr.co.jeelee.kiwee.domain.channel.exception.PlatformChannelNotFoundException;
import kr.co.jeelee.kiwee.domain.channel.repository.PlatformChannelRepository;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlatformChannelServiceImpl implements PlatformChannelService {

	private final PlatformChannelRepository platformChannelRepository;

	@Override
	public void addPlatformChannel(Platform platform, Channel channel, String platformChannelId) {

		if (platformChannelRepository.existsByPlatformAndPlatformChannelId(platform, platformChannelId)) {
			throw new ChannelAlreadyExistException();
		}

		PlatformChannel platformChannel = PlatformChannel.of(platformChannelId, platform, channel);
		platformChannelRepository.save(platformChannel);
	}

	@Override
	public void deletePlatformChannel(Platform platform, String platformChannelId) {
		PlatformChannel pc = platformChannelRepository.findByPlatformAndPlatformChannelId(platform, platformChannelId)
			.orElseThrow(PlatformChannelNotFoundException::new);

		platformChannelRepository.delete(pc);
	}
}
