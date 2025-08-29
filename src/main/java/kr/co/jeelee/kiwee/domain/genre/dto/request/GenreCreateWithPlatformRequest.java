package kr.co.jeelee.kiwee.domain.genre.dto.request;

import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record GenreCreateWithPlatformRequest(
	UUID platformId, String platformProvider,
	@NotNull(message = "idInPlatform can't be Null.") String idInPlatform,
	@Valid @NotNull(message = "genre can't be Null.") GenreCreateRequest genre
) {
}
