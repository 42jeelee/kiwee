package kr.co.jeelee.kiwee.domain.genre.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.genre.dto.response.GenreResponse;
import kr.co.jeelee.kiwee.domain.genre.entity.Genre;
import kr.co.jeelee.kiwee.domain.genre.exception.GenreNotFoundException;
import kr.co.jeelee.kiwee.domain.genre.repository.GenreRepository;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreServiceImpl implements GenreService {

	private final GenreRepository genreRepository;

	@Override
	public Genre getOrCreateGenre(String name) {
		return genreRepository.findByName(name)
			.orElseGet(() -> genreRepository.save(Genre.of(name)));
	}

	@Override
	public PagedResponse<GenreResponse> getAllGenre(Pageable pageable) {
		return PagedResponse.of(
			genreRepository.findAll(pageable),
			GenreResponse::from
		);
	}

	@Override
	public GenreResponse changeName(Long id, String name) {
		Genre genre = genreRepository.findById(id)
			.orElseThrow(GenreNotFoundException::new);

		genre.changeName(name);
		return GenreResponse.from(genre);
	}
}
