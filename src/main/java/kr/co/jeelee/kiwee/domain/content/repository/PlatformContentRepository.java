package kr.co.jeelee.kiwee.domain.content.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.content.entity.PlatformContent;

public interface PlatformContentRepository extends JpaRepository<PlatformContent, Long> {

	boolean existsByPlatformIdAndIdInPlatform(UUID platformId, String idInPlatform);

	Optional<PlatformContent> findByPlatformIdAndIdInPlatform(UUID platformId, String idInPlatform);

}
