package kr.co.jeelee.kiwee.domain.platform.dto.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;

public record PlatformCreateRequest(
	@NotBlank(message = "name can't be Blank.") String name,
	@URL(message = "This is not in URL format.") String icon,
	@URL(message = "This is not in URL format.") String banner,
	String description, String homepage, String provider, boolean isToken
) {
}
