package kr.co.jeelee.kiwee.domain.genre.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.genre.entity.PlatformGenre;

public interface PlatformGenreRepository extends JpaRepository<PlatformGenre, Long> {

	Optional<PlatformGenre> findByPlatformIdAndIdInPlatform(UUID platformId, String idInPlatform);
}
