package kr.co.jeelee.kiwee.domain.content.dto.response;

import java.util.List;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.content.model.ContentLevel;
import kr.co.jeelee.kiwee.global.model.DataProvider;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.domain.genre.dto.response.GenreResponse;
import kr.co.jeelee.kiwee.domain.platform.dto.response.PlatformSimpleResponse;

public record ContentDetailResponse(
	UUID id, DataProvider sourceProvider, String sourceId, String title, String originalTitle,
	String description, Double rating, String imageUrl, String homePage,
	ContentType contentType, ContentLevel contentLevel, ContentSimpleResponse parent,
	List<GenreResponse> genres, List<PlatformSimpleResponse> platforms
) {
	public static ContentDetailResponse from(Content content) {
		return new ContentDetailResponse(
			content.getId(),
			content.getSourceProvider(),
			content.getSourceId(),
			content.getTitle(),
			content.getOriginalTitle(),
			content.getDescription(),
			content.getRating(),
			content.getImageUrl(),
			content.getHomePage(),
			content.getContentType(),
			content.getContentLevel(),
			content.getParent() != null
			? ContentSimpleResponse.from(content.getParent())
			: null,
			content.getGenres().stream()
				.map(GenreResponse::from)
				.toList(),
			content.getPlatforms().stream()
				.map(PlatformSimpleResponse::from)
				.toList()
		);
	}
}
