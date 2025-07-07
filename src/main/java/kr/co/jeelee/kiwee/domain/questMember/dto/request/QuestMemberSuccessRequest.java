package kr.co.jeelee.kiwee.domain.questMember.dto.request;

import org.hibernate.validator.constraints.URL;

import kr.co.jeelee.kiwee.global.model.MediaType;

public record QuestMemberSuccessRequest(
	MediaType mediaType,
	@URL(message = "THis is not a URL format.") String contentUrl,
	String message
) {
}
