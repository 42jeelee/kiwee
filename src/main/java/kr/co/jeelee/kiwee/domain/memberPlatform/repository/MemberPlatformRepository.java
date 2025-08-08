package kr.co.jeelee.kiwee.domain.memberPlatform.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.jeelee.kiwee.domain.memberPlatform.entity.MemberPlatform;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;

public interface MemberPlatformRepository extends JpaRepository<MemberPlatform,Long> {

	@EntityGraph(attributePaths = {"member"})
	@Query("""
		SELECT mp FROM MemberPlatform mp
		JOIN FETCH mp.member m
		LEFT JOIN FETCH m.roles r
		LEFT JOIN FETCH r.permissions
		where mp.platform = :platform and mp.platformUserId = :platformUserId
	""")
	Optional<MemberPlatform> getWithAll(
		@Param("platform") Platform platform,
		@Param("platformUserId") String platformUserId
	);

	Page<MemberPlatform> findByMemberIdAndPlatformId(UUID memberId, UUID platformId, Pageable pageable);
	Page<MemberPlatform> findByMemberId(UUID memberId, Pageable pageable);
	Page<MemberPlatform> findByPlatformId(UUID platformId, Pageable pageable);

}
