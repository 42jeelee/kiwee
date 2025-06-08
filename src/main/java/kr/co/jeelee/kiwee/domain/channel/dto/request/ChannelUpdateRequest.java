package kr.co.jeelee.kiwee.domain.channel.dto.request;

import org.hibernate.validator.constraints.URL;

public record ChannelUpdateRequest(
	String name,
	@URL(message = "This is not in URL format.") String icon,
	@URL(message = "This is not in URL format.") String banner,
	String description,
	Boolean isPubic
) {
}
