package kr.co.jeelee.kiwee.domain.content.dto.request;

import java.util.Set;
import java.util.UUID;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;

public record ContentCreateRequest(
	@NotBlank(message = "title can't be Blank.") String title,
	@NotBlank(message = "overview can't be Blank.") String overview,
	@NotNull(message = "rating can't be Null.") Double rating,
	@URL(message = "This is not a URL format.") String imageUrl,
	@URL(message = "This is not a URL format.") String homepage,
	Long totalAmount,
	@NotNull(message = "contentType can't be Null.") ContentType contentType,
	UUID parentId,
	Long childrenIdx,
	Set<Long> genres
) {
}
