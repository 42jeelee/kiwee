package kr.co.jeelee.kiwee.domain.auth.service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import kr.co.jeelee.kiwee.domain.auth.dto.TokenType;
import kr.co.jeelee.kiwee.domain.auth.dto.response.TokenResponse;
import kr.co.jeelee.kiwee.domain.auth.exception.InvalidTokenException;
import kr.co.jeelee.kiwee.domain.auth.exception.UnauthorizationException;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.global.config.JwtConfig;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

	private final JwtEncoder jwtEncoder;
	private final JwtDecoder jwtDecoder;
	private final RedisTemplate<String, String> redisTemplate;

	private final MemberService memberService;

	private static final String REFRESH_PREFIX = "refresh:";

	@Override
	public TokenResponse createToken(Authentication authentication) {
		return TokenResponse.of(
			generateAccessToken(authentication),
			generateRefreshToken(authentication)
		);
	}

	@Override
	public TokenResponse refresh(String refreshToken) {
		if (!isRefreshTokenValid(refreshToken)) {
			throw new InvalidTokenException();
		}

		UUID memberId = UUID.fromString(getSubject(refreshToken));
		Authentication authentication = memberService.toAuthentication(memberId);

		removeRefreshToken(memberId);

		return TokenResponse.of(
			generateAccessToken(authentication),
			generateRefreshToken(authentication)
		);
	}

	@Override
	public String getSubject(String token) {
		try {
			return jwtDecoder.decode(token).getSubject();
		} catch (JwtException | IllegalArgumentException e) {
			throw new InvalidTokenException();
		}
	}

	@Override
	public void removeRefreshToken(UUID memberId) {
		redisTemplate.delete(REFRESH_PREFIX + memberId.toString());
	}

	private String generateAccessToken(Authentication authentication) {
		return generateToken(authentication, TokenType.ACCESS, null);
	}

	private String generateRefreshToken(Authentication authentication) {
		Duration ttl = Duration.ofDays(7);
		String token = generateToken(authentication, TokenType.REFRESH, ttl);

		redisTemplate.opsForValue().set(
			REFRESH_PREFIX + getMemberId(authentication),
			token,
			ttl
		);

		return token;
	}

	private String generateToken(Authentication authentication, TokenType tokenType, Duration ttl) {
		UUID memberId = getMemberId(authentication);
		Instant now = Instant.now();
		Instant expiresAt = now.plus(
			ttl != null ? ttl : Duration.ofMinutes(30)
		);

		JwsHeader headers = JwsHeader.with(() -> "HS256")
			.keyId(JwtConfig.MAIN_KEY_ID)
			.build();

		JwtClaimsSet claims = JwtClaimsSet.builder()
			.subject(memberId.toString())
			.issuedAt(now)
			.expiresAt(expiresAt)
			.claim("type", tokenType.name().toLowerCase())
			.build();

		return jwtEncoder.encode(JwtEncoderParameters.from(headers, claims)).getTokenValue();
	}

	private UUID getMemberId(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new UnauthorizationException();
		}

		String id = authentication.getName();
		return UUID.fromString(id);
	}

	private boolean isRefreshTokenValid(String token) {
		try {
			String memberId = getSubject(token);

			String stored = redisTemplate.opsForValue().get(REFRESH_PREFIX + memberId);
			return stored != null && stored.equals(token);
		} catch (InvalidTokenException e) {
			return false;
		}
	}

}
