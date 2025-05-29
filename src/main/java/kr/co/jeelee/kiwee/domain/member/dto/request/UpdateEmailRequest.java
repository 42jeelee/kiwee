package kr.co.jeelee.kiwee.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateEmailRequest(
	@Email(message = "This is not an email format.")
	@NotBlank(message = "Email can't be Blank.")
	String email
) {}
