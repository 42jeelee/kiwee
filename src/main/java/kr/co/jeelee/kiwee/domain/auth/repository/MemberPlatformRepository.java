package kr.co.jeelee.kiwee.domain.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.auth.entity.MemberPlatform;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;

public interface MemberPlatformRepository extends JpaRepository<MemberPlatform,Long> {

	@EntityGraph(attributePaths = {"member"})
	Optional<MemberPlatform> getWithMemberByPlatformAndPlatformUserId(Platform platform, String platformUserId);

}
