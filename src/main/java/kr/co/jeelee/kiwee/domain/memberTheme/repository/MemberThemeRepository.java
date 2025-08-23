package kr.co.jeelee.kiwee.domain.memberTheme.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.memberTheme.entity.MemberTheme;

public interface MemberThemeRepository extends JpaRepository<MemberTheme, UUID> {

	Optional<MemberTheme> findByMemberId(UUID memberId);

}
