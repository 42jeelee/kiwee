package kr.co.jeelee.kiwee.domain.genre.dto.response;

import kr.co.jeelee.kiwee.domain.genre.entity.Genre;

public record GenreResponse(
	Long id, String name
) {
	public static GenreResponse from(Genre genre) {
		return new GenreResponse(
			genre.getId(),
			genre.getName()
		);
	}
}
