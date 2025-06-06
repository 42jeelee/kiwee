package kr.co.jeelee.kiwee.domain.auth.oauth.dto;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;

import kr.co.jeelee.kiwee.domain.auth.oauth.exception.ProviderNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OAuthAttributes {
	GOOGLE("google", (userNameAttributeName, attributes) -> OAuth2UserInfo.of(
		"google",
		userNameAttributeName,
		"name",
		"picture",
		"email",
		"email_verified",
		attributes
	)),
	DISCORD("discord", (userNameAttributeName, attributes) -> OAuth2UserInfo.of(
		"discord",
		userNameAttributeName,
		"global_name",
		"avatar",
		"email",
		"verified",
		attributes
	)),

	;

	private final String registrationId;
	private final BiFunction<String, Map<String, Object>, OAuth2UserInfo> of;

	public static OAuth2UserInfo of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
		return Arrays.stream(values())
			.filter(provider -> provider.registrationId.equals(registrationId))
			.findFirst()
			.map(provider -> provider.of.apply(userNameAttributeName, attributes))
			.orElseThrow(ProviderNotFoundException::new);
	}
}
