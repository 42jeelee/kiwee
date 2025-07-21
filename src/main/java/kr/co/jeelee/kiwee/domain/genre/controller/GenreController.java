package kr.co.jeelee.kiwee.domain.genre.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.genre.dto.request.GenreCreateRequest;
import kr.co.jeelee.kiwee.domain.genre.dto.response.GenreResponse;
import kr.co.jeelee.kiwee.domain.genre.service.GenreService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/genres")
@RequiredArgsConstructor
@Validated
public class GenreController {

	private final GenreService genreService;

	@PreAuthorize(value = "hasRole('CREATE_GENRE')")
	@PostMapping
	public GenreResponse createGenre(
		@Valid @RequestBody GenreCreateRequest request
	) {
		return genreService.createGenre(request);
	}

	@GetMapping
	public PagedResponse<GenreResponse> getAll(
		@PageableDefault Pageable pageable
	) {
		return genreService.getAllGenre(pageable);
	}

	@PreAuthorize(value = "hasRole('EDIT_GENRE')")
	@PatchMapping(value = "/{id}")
	public GenreResponse changeName(
		@PathVariable Long id,
		@RequestParam String name
	) {
		return genreService.changeName(id, name);
	}

}
