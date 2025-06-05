package kr.co.jeelee.kiwee.domain.member.dto.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberCreateRequest(
	@NotBlank(message = "Name can't be Blank.") String name,
	String nickname,
	@Email(message = "This is not an email format.") String email,
	@URL(message = "This is not in URL format.") String avatarUrl
) {}
