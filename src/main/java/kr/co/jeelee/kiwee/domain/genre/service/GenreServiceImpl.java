package kr.co.jeelee.kiwee.domain.genre.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.genre.dto.request.GenreCreateRequest;
import kr.co.jeelee.kiwee.domain.genre.dto.request.GenreCreateWithPlatformRequest;
import kr.co.jeelee.kiwee.domain.genre.dto.response.GenreResponse;
import kr.co.jeelee.kiwee.domain.genre.entity.Genre;
import kr.co.jeelee.kiwee.domain.genre.entity.PlatformGenre;
import kr.co.jeelee.kiwee.domain.genre.exception.GenreNotFoundException;
import kr.co.jeelee.kiwee.domain.genre.repository.GenreRepository;
import kr.co.jeelee.kiwee.domain.genre.repository.PlatformGenreRepository;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;
import kr.co.jeelee.kiwee.domain.platform.service.PlatformService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreServiceImpl implements GenreService {

	private final GenreRepository genreRepository;
	private final PlatformGenreRepository platformGenreRepository;

	private final PlatformService platformService;

	@Override
	@Transactional
	public Genre getOrCreateGenre(GenreCreateWithPlatformRequest request) {
		System.out.println(request);
		if ((request.platformId() == null) == (request.platformProvider() == null)) {
			throw new FieldValidationException("platform", "platformId 또는 platformProvider 중 하나만 존재해야 합니다.");
		}
		Platform platform = request.platformId() != null
			? platformService.getById(request.platformId())
			: platformService.getEntityByProvider(request.platformProvider());

		return platformGenreRepository.findByPlatformIdAndIdInPlatform(request.platformId(), request.idInPlatform())
			.map(PlatformGenre::getGenre)
			.orElseGet(() -> {
				Genre genre = genreRepository.findByName(request.genre().name())
						.orElseGet(() -> genreRepository.save(Genre.of(request.genre().name())));
				platformGenreRepository.save(
					PlatformGenre.of(platform, genre, request.idInPlatform(), request.genre().name())
				);
				return genre;
			});
	}

	@Override
	@Transactional
	public GenreResponse createGenre(GenreCreateRequest request) {
		if (genreRepository.existsByName(request.name())) {
			throw new FieldValidationException("name", "이미 존재하는 장르입니다.");
		}

		Genre genre = Genre.of(request.name());

		return GenreResponse.from(genreRepository.save(genre));
	}

	@Override
	public Genre getById(Long id) {
		return genreRepository.findById(id)
			.orElseThrow(GenreNotFoundException::new);
	}

	@Override
	public Genre getByPlatform(UUID platformId, String idInPlatform) {
		return platformGenreRepository.findByPlatformIdAndIdInPlatform(platformId, idInPlatform)
			.map(PlatformGenre::getGenre)
			.orElseThrow(GenreNotFoundException::new);
	}

	@Override
	public PagedResponse<GenreResponse> getAllGenre(Pageable pageable) {
		return PagedResponse.of(
			genreRepository.findAll(pageable),
			GenreResponse::from
		);
	}

	@Override
	@Transactional
	public GenreResponse changeName(Long id, String name) {
		Genre genre = genreRepository.findById(id)
			.orElseThrow(GenreNotFoundException::new);

		genre.changeName(name);
		return GenreResponse.from(genre);
	}
}
