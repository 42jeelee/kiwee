package kr.co.jeelee.kiwee.domain.channelMember.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.jeelee.kiwee.domain.authorization.model.PermissionType;
import kr.co.jeelee.kiwee.domain.authorization.model.RoleType;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.channelMember.entity.ChannelMember;
import kr.co.jeelee.kiwee.domain.member.entity.Member;

@Repository
public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long> {

	@Query("""
		SELECT cm FROM ChannelMember cm
		JOIN FETCH cm.channel
		JOIN FETCH cm.member
		WHERE cm.channel.id = :channelId AND cm.member.id = :memberId
	""")
	Optional<ChannelMember> getWithChannelAndMemberByChannelIdAndMemberId(
		@Param("channelId") UUID channelId,
		@Param("memberId") UUID memberId
	);

	Optional<ChannelMember> findByMemberIdAndChannelId(UUID memberId, UUID channelId);

	Page<ChannelMember> getChannelMembersByChannel(Channel channel, Pageable pageable);

	@Query("""
		SELECT cm.isBen FROM ChannelMember cm
		WHERE cm.channel = :channel AND cm.member = :member
	""")
	Boolean isBenByChannelAndMember(@Param("channel") Channel channel, @Param("member") Member member);

	Boolean existsByChannelAndMember(Channel channel, Member member);

	@Query("""
		SELECT COUNT(cmr) > 0
		FROM ChannelMemberRole cmr
		JOIN cmr.channelMember cm
		WHERE cm.channel = :channel AND cm.member = :member AND cmr.role.name = :roleType
	""")
	Boolean hasRoleType(Channel channel, Member member, RoleType roleType);

	@Query(value = """
		SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
		FROM ChannelMemberRole cmr
		JOIN cmr.channelMember cm
		JOIN cmr.role r
		JOIN r.permissions p
		WHERE cm.channel.id = :channelId
		AND cm.member.id = :memberId
		AND p.name = :permissionType
	""")
	Boolean hasPermissionType(
		@Param("channelId") UUID channel,
		@Param("memberId") UUID member,
		@Param("permissionType") PermissionType permissionType
	);

}
