package kr.co.jeelee.kiwee.domain.genre.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.genre.dto.request.GenreCreateRequest;
import kr.co.jeelee.kiwee.domain.genre.dto.request.GenreCreateWithPlatformRequest;
import kr.co.jeelee.kiwee.domain.genre.dto.response.GenreResponse;
import kr.co.jeelee.kiwee.domain.genre.entity.Genre;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface GenreService {

	Genre getOrCreateGenre(GenreCreateWithPlatformRequest request);

	GenreResponse createGenre(GenreCreateRequest request);

	Genre getById(Long id);

	Genre getByPlatform(UUID platformId, String idInPlatform);

	PagedResponse<GenreResponse> getAllGenre(Pageable pageable);

	GenreResponse changeName(Long id, String name);

}
