package kr.co.jeelee.kiwee.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.jeelee.kiwee.domain.auth.dto.request.RefreshTokenRequest;
import kr.co.jeelee.kiwee.domain.auth.dto.response.TokenResponse;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.auth.service.JwtService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

	private final JwtService jwtService;

	@PostMapping(value = "/refresh")
	public TokenResponse refresh(
		@RequestBody @Valid RefreshTokenRequest request
	) {
		return jwtService.refresh(request.refreshToken());
	}

	@PostMapping(value = "/logout")
	public ResponseEntity<Void> logout(
		@AuthenticationPrincipal CustomOAuth2User principal
	) {
		jwtService.removeRefreshToken(principal.member().getId());
		return ResponseEntity.noContent().build();
	}

}
