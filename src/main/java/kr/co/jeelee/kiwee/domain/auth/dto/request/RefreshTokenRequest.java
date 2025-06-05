package kr.co.jeelee.kiwee.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
	@NotBlank(message = "refresh token can't be Blank.") String refreshToken
) {
}
