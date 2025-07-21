package kr.co.jeelee.kiwee.domain.platform.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.platform.entity.Platform;

public interface PlatformRepository extends JpaRepository<Platform, UUID> {

	boolean existsByName(String name);

	Page<Platform> findByNameContaining(String name, Pageable pageable);

	Optional<Platform> findByProvider(String provider);

}
