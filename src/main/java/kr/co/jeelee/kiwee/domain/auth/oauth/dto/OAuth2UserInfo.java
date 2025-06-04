package kr.co.jeelee.kiwee.domain.auth.oauth.dto;

import java.util.Map;

public record OAuth2UserInfo(
	String provider, String id,
	String name, String avatarUrl,
	Map<String, Object> attributes
) {
	public static OAuth2UserInfo of(
		String provider,
		String id,
		String name,
		String avatarUrl,
		Map<String, Object> attributes
	) {
		return new OAuth2UserInfo(provider, id, name, avatarUrl, attributes);
	}
}
