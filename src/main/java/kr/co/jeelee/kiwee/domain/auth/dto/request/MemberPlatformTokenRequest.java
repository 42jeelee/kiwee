package kr.co.jeelee.kiwee.domain.auth.dto.request;

import java.time.LocalDateTime;

public record MemberPlatformTokenRequest(
	Boolean isToken, String accessToken, String refreshToken,
	LocalDateTime tokenExpireAt
) {
}
