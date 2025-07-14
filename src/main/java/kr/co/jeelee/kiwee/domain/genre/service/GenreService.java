package kr.co.jeelee.kiwee.domain.genre.service;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.genre.dto.response.GenreResponse;
import kr.co.jeelee.kiwee.domain.genre.entity.Genre;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface GenreService {

	Genre getOrCreateGenre(String name);

	PagedResponse<GenreResponse> getAllGenre(Pageable pageable);

	GenreResponse changeName(Long id, String name);

}
