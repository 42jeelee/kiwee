package kr.co.jeelee.kiwee.domain.auth.oauth.user;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import kr.co.jeelee.kiwee.domain.auth.oauth.dto.OAuth2UserInfo;
import kr.co.jeelee.kiwee.domain.member.entity.Member;

public record CustomOAuth2User(
	Member member,
	OAuth2UserInfo oAuth2UserInfo
) implements OAuth2User, UserDetails {

	@Override
	public Map<String, Object> getAttributes() {
		return oAuth2UserInfo.attributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return member.getRoles().stream()
			.flatMap(role -> role.getPermissions().stream())
			.map(permission -> new SimpleGrantedAuthority(permission.getName().name()))
			.collect(Collectors.toSet());
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public String getUsername() {
		return member.getId().toString();
	}

	@Override
	public String getName() {
		return member.getId().toString();
	}

	@Override
	public boolean isEnabled() {
		return member.isActive();
	}

	public static CustomOAuth2User from(Member member, OAuth2UserInfo oAuth2UserInfo) {
		return new CustomOAuth2User(member, oAuth2UserInfo);
	}
}
