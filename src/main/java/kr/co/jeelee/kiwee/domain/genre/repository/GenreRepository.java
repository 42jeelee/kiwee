package kr.co.jeelee.kiwee.domain.genre.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.genre.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

	boolean existsByName(String name);

	Optional<Genre> findByName(String name);

}
