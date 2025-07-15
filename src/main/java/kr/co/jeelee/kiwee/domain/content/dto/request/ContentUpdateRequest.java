package kr.co.jeelee.kiwee.domain.content.dto.request;

import java.util.Set;
import java.util.UUID;

import org.hibernate.validator.constraints.URL;

import kr.co.jeelee.kiwee.domain.content.model.ContentLevel;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;

public record ContentUpdateRequest(
	String title, String description, Double rating,
	@URL(message = "This is not a URL format.") String imageUrl,
	@URL(message = "This is not a URL format.") String homePage,
	ContentType contentType, ContentLevel contentLevel, UUID parent,
	Set<String> genres, Set<UUID> platformIds, String applicationId
) {
}
