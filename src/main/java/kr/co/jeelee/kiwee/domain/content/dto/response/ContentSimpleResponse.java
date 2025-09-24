package kr.co.jeelee.kiwee.domain.content.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;

public record ContentSimpleResponse(
	UUID id, String title, String description, String imageUrl,
	Long totalAmount, ContentType contentType,
	ContentSimpleResponse series, Long childrenIdx,
	ContentReactSummary reactSummary, LocalDateTime createdAt
) {
	public static ContentSimpleResponse from(Content content, ContentReactSummary reactSummary) {
		return new ContentSimpleResponse(
			content.getId(),
			content.getTitle(),
			content.getOverview(),
			content.getImageUrl(),
			content.getTotalAmount(),
			content.getContentType(),
			content.getParent() != null
				? ContentSimpleResponse.from(content.getParent())
				: null,
			content.getChildrenIdx(),
			reactSummary,
			content.getCreatedAt()
		);
	}

	public static ContentSimpleResponse from(Content content) {
		return from(content, null);
	}
}
