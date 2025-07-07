package kr.co.jeelee.kiwee.domain.platform.dto.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.global.model.DataProvider;

public record PlatformCreateRequest(
	@NotBlank(message = "name can't be Blank.") String name,
	@URL(message = "This is not in URL format.") String icon,
	@URL(message = "This is not in URL format.") String banner,
	@NotNull(message = "sourceProvider can't be Null.") DataProvider sourceProvider,
	String sourceId, String description, String homePage, String provider, boolean isToken
) {
}
