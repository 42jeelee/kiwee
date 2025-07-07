package kr.co.jeelee.kiwee.domain.platform.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.platform.entity.Platform;
import kr.co.jeelee.kiwee.global.model.DataProvider;

public interface PlatformRepository extends JpaRepository<Platform, UUID> {

	boolean existsByName(String name);

	boolean existsBySourceProviderAndSourceId(DataProvider sourceProvider, String sourceId);

	Page<Platform> findByNameContaining(String name, Pageable pageable);

	Optional<Platform> findByProvider(String provider);

	Optional<Platform> findBySourceProviderAndSourceId(DataProvider sourceProvider, String sourceId);

}
