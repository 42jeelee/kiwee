package kr.co.jeelee.kiwee.domain.content.dto.response;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.content.entity.Content;

public record ContentSimpleResponse(
	UUID id, String title, String description, String imageUrl, ContentSimpleResponse series
) {
	public static ContentSimpleResponse from(Content content) {
		return new ContentSimpleResponse(
			content.getId(),
			content.getTitle(),
			content.getOverview(),
			content.getImageUrl(),
			content.getParent() != null
				? ContentSimpleResponse.from(content.getParent())
				: null
		);
	}
}
