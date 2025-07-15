package kr.co.jeelee.kiwee.domain.content.dto.request;

import java.util.Set;
import java.util.UUID;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.domain.content.model.ContentLevel;
import kr.co.jeelee.kiwee.global.model.DataProvider;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;

public record ContentCreateRequest(
	@NotNull(message = "dataProvider can't be Null.") DataProvider dataProvider,
	String sourceId,
	@NotBlank(message = "title can't be Blank.") String title,
	@NotBlank(message = "originalTitle can't be Blank.") String originalTitle,
	@NotBlank(message = "description can't be Blank.") String description,
	@NotNull(message = "rating can't be Null.") Double rating,
	@URL(message = "This is not a URL format.") String imageUrl,
	@URL(message = "This is not a URL format.") String homePage,
	@NotNull(message = "contentType can't be Null.") ContentType contentType,
	@NotNull(message = "contentLevel can't be Null.") ContentLevel contentLevel,
	UUID parentId,
	@NotNull(message = "genres can't be Null.") Set<String> genres,
	@NotNull(message = "platformIds can't be Null.") Set<UUID> platformIds,
	String applicationId
) {
}
