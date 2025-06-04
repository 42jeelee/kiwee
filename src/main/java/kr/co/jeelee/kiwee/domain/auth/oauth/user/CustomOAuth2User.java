package kr.co.jeelee.kiwee.domain.auth.oauth.user;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import kr.co.jeelee.kiwee.domain.auth.oauth.dto.OAuth2UserInfo;
import kr.co.jeelee.kiwee.domain.member.entity.Member;

public record CustomOAuth2User(
	Member member,
	OAuth2UserInfo oAuth2UserInfo
) implements OAuth2User {

	@Override
	public Map<String, Object> getAttributes() {
		return oAuth2UserInfo.attributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getName() {
		return member.getId().toString();
	}

	public static CustomOAuth2User from(Member member, OAuth2UserInfo oAuth2UserInfo) {
		return new CustomOAuth2User(member, oAuth2UserInfo);
	}
}
