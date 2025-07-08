package kr.co.jeelee.kiwee.domain.task.metadata;

import org.hibernate.validator.constraints.URL;

import kr.co.jeelee.kiwee.global.model.MediaType;

public record CheckInMetadata(
	MediaType mediaType,
	@URL(message = "This is not a URL Format.") String mediaUrl,
	String message
) {
}
