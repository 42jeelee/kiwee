package kr.co.jeelee.kiwee.domain.auth.dto.response;

public record AccessTokenResponse(
	String accessToken
) {
	public static AccessTokenResponse from(String accessToken) {
		return new AccessTokenResponse(accessToken);
	}
}
