package kr.co.jeelee.kiwee.domain.auth.oauth.dto;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public record OAuth2UserInfo(
	String provider, String id, String name,
	String avatarUrl, String email,
	Map<String, Object> attributes
) {
	public static OAuth2UserInfo of(
		String provider,
		String idField,
		String nameField,
		Supplier<String> getAvatarUrl,
		String emailField,
		String emailVerifiedField,
		Map<String, Object> attributes
	) {
		Boolean isVerified = Optional.ofNullable(attributes.get(emailVerifiedField))
			.map(v -> {
				if (v instanceof Boolean b) return b;
				if (v instanceof String s) return Boolean.parseBoolean(s);
				return false;
			}).orElse(false);

		String email = isVerified
			? Optional.ofNullable(attributes.get(emailField))
			.map(Object::toString).orElse(null)
			: null;

		return new OAuth2UserInfo(
			provider,
			attributes.get(idField).toString(),
			attributes.get(nameField).toString(),
			getAvatarUrl.get(),
			email,
			attributes);
	}
}
