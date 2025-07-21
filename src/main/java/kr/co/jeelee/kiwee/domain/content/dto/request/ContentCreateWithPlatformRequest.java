package kr.co.jeelee.kiwee.domain.content.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.domain.genre.dto.request.GenreCreateWithPlatformRequest;

public record ContentCreateWithPlatformRequest(
	@NotBlank(message = "idInPlatform can't be Blank.") String idInPlatform,
	@Valid @NotNull(message = "content can't be Null.") ContentCreateRequest content,
	List<@Valid GenreCreateWithPlatformRequest> genres
) {
}
