package kr.co.jeelee.kiwee.domain.content.dto.response;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.domain.genre.dto.response.GenreResponse;

public record ContentDetailResponse(
	UUID id, String title, String overview, Double rating, String imageUrl,
	String homepage, Long totalAmount, ContentType contentType,
	ContentSimpleResponse series, Set<GenreResponse> genres,
	LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static ContentDetailResponse from(Content content) {
		return new ContentDetailResponse(
			content.getId(),
			content.getTitle(),
			content.getOverview(),
			content.getRating(),
			content.getImageUrl(),
			content.getHomepage(),
			content.getTotalAmount(),
			content.getContentType(),
			content.getParent() != null
				? ContentSimpleResponse.from(content.getParent())
				: null,
			content.getGenres() != null
				? content.getGenres().stream()
					.map(GenreResponse::from)
					.collect(Collectors.toSet())
				: null,
			content.getUpdatedAt(),
			content.getCreatedAt()
		);
	}
}
