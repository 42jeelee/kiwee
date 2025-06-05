package kr.co.jeelee.kiwee.domain.auth.service;

import java.util.UUID;

import org.springframework.security.core.Authentication;

import kr.co.jeelee.kiwee.domain.auth.dto.response.TokenResponse;

public interface JwtService {

	TokenResponse createToken(Authentication authentication);

	TokenResponse refresh(String refreshToken);

	String getSubject(String token);

	void removeRefreshToken(UUID memberId);
}
