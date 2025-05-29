package kr.co.jeelee.kiwee.domain.member.dto.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;

public record UpdateAvatarRequest(
	@NotBlank(message = "avatarURL can't be Blank.")
	@URL(message = "This is not in URL format.")
	String avatarUrl
) {}
