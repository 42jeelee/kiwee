package kr.co.jeelee.kiwee.domain.questMember.dto.request;

import java.util.UUID;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotNull;

public record QuestMemberVerificationRequest(
	String contentType,
	@URL(message = "This is not in URL format.") String contentUrl,
	String message
) {
}
