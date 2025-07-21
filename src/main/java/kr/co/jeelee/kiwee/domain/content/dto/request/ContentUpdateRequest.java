package kr.co.jeelee.kiwee.domain.content.dto.request;

import java.util.Set;
import java.util.UUID;

import org.hibernate.validator.constraints.URL;

import kr.co.jeelee.kiwee.domain.content.model.ContentType;

public record ContentUpdateRequest(
	String title, String overview, Double rating,
	@URL(message = "This is not a URL format.") String imageUrl,
	@URL(message = "This is not a URL format.") String homepage,
	ContentType contentType, UUID parentId, Set<Long> genreIds
) {
}
