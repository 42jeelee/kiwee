package kr.co.jeelee.kiwee.domain.auth.oauth.dto;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

import kr.co.jeelee.kiwee.domain.auth.oauth.exception.ProviderNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OAuthAttributes {
	GOOGLE("google", (userNameAttributeName, attributes) -> OAuth2UserInfo.of(
		"google",
		userNameAttributeName,
		"name",
		() -> Optional.ofNullable(attributes.get("picture")).map(Objects::toString).orElse(null),
		"email",
		"email_verified",
		attributes
	)),
	DISCORD("discord", (userNameAttributeName, attributes) -> OAuth2UserInfo.of(
		"discord",
		userNameAttributeName,
		"global_name",
		() -> {
			String baseUrl = "https://cdn.discordapp.com/avatars/";
			String userId = attributes.get(userNameAttributeName).toString();
			String hash = Optional.ofNullable(attributes.get("avatar")).map(Objects::toString).orElse(null);

			return hash != null
				? baseUrl + userId + "/" + hash + (hash.startsWith("a_") ? ".gif" : ".png")
				: null;
		},
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
