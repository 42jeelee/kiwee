package kr.co.jeelee.kiwee.domain.channel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.channel.entity.PlatformChannel;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;

public interface PlatformChannelRepository extends JpaRepository<PlatformChannel, Long> {

	boolean existsByPlatformAndPlatformChannelId(Platform platform, String platformChannelId);

	Optional<PlatformChannel> findByPlatformAndPlatformChannelId(Platform platform, String platformChannelId);

}
