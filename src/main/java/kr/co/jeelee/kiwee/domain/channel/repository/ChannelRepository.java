package kr.co.jeelee.kiwee.domain.channel.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.jeelee.kiwee.domain.channel.dto.response.ChannelSimpleResponse;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

	@EntityGraph(attributePaths = {"platformChannels.platform"})
	@Query("SELECT c FROM Channel c WHERE c.id = :id")
	Optional<Channel> findWithPlatformsById(@Param("id") UUID id);

	boolean existsByName(String name);

	@Query("""
		SELECT DISTINCT new kr.co.jeelee.kiwee.domain.channel.dto.response.ChannelSimpleResponse(
			c.id, c.name, c.icon, c.banner, c.isPublic,
			CASE WHEN cm.id IS NOT NULL THEN TRUE ELSE FALSE END
		)
		FROM Channel c
		LEFT JOIN ChannelMember cm ON cm.channel = c AND cm.member.id = :memberId
		WHERE c.isPublic = TRUE OR cm IS NOT NULL
	""")
	Page<ChannelSimpleResponse> findPublicAndJoinChannels(@Param("memberId") UUID memberId, Pageable pageable);

}
