package kr.co.jeelee.kiwee.domain.platform.dto.request;

import org.hibernate.validator.constraints.URL;

public record PlatformUpdateRequest(
	String name,
	@URL(message = "This is not in URL format.") String icon,
	@URL(message = "This is not in URL format.") String banner,
	String description,
	@URL(message = "This is not in URL format.") String page
) {
}
