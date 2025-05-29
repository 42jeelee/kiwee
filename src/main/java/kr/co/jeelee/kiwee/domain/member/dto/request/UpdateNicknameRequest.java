package kr.co.jeelee.kiwee.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateNicknameRequest(
	@NotBlank(message = "Nickname can't be Blank.")
	@Size(min = 1, max = 40, message = "Nickname length must be between 1 and 40 characters.")
	String nickname
) {}
