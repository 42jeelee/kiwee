package kr.co.jeelee.kiwee.domain.content.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.content.entity.PlatformContent;

public interface PlatformContentRepository extends JpaRepository<PlatformContent, Long> {

	boolean existsByPlatformIdAndIdInPlatform(UUID platformId, String idInPlatform);

	Optional<PlatformContent> findByPlatformIdAndIdInPlatform(UUID platformId, String idInPlatform);

	Optional<PlatformContent> findByPlatform_ProviderAndIdInPlatform(String platformProvider, String idInPlatform);

	Optional<PlatformContent> findByPlatform_IdAndContent_Id(UUID platformId, UUID contentId);

	Optional<PlatformContent> findByPlatform_ProviderAndContent_Id(String provider, UUID contentId);

	Page<PlatformContent> findByContent_Id(UUID contentId, Pageable pageable);

}
