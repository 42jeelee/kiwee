package kr.co.jeelee.kiwee.domain.genre.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GenreCreateRequest(
	@NotBlank(message = "name can't be Blank.") String name
) {
}
