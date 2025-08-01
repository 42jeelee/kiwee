package kr.co.jeelee.kiwee.domain.auth.converter;

import java.util.UUID;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import kr.co.jeelee.kiwee.domain.auth.model.TokenType;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	private final MemberService memberService;

	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		if (!jwt.getClaim("type").equals(TokenType.ACCESS.name().toLowerCase())) {
			throw new BadCredentialsException("유효한 access 토큰이 아닙니다.");
		}

		UUID memberId = UUID.fromString(jwt.getSubject());
		Member member = memberService.getById(memberId);

		CustomOAuth2User oAuth2User = CustomOAuth2User.from(member, null);

		return new UsernamePasswordAuthenticationToken(oAuth2User, "", oAuth2User.getAuthorities());
	}
}
