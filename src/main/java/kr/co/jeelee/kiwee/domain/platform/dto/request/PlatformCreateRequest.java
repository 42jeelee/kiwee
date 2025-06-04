package kr.co.jeelee.kiwee.domain.platform.dto.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;

public record PlatformCreateRequest(
	@NotBlank(message = "name can't be Blank.") String name,
	@URL(message = "This is not in URL format.")
	@NotBlank(message = "icon can't be Blank.") String icon,
	@URL(message = "This is not in URL format.")
	@NotBlank(message = "banner can't be Blank.") String banner,
	String description, String page, String provider, boolean isToken
) {
}
