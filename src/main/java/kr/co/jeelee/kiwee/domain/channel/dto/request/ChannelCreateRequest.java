package kr.co.jeelee.kiwee.domain.channel.dto.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChannelCreateRequest(
	@NotBlank(message = "name can't be Blank.") String name,
	@URL(message = "This is not in URL format.")
	@NotBlank(message = "icon can't be Blank.")
	String icon,

	@URL(message = "This is not in URL format.")
	@NotBlank(message = "icon can't be Blank.")
	String banner,

	String description,
	@NotNull(message = "isOriginal is required.") Boolean isOriginal,
	@NotNull(message = "isPublic is required.") Boolean isPublic
) {
}
