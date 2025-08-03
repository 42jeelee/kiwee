package kr.co.jeelee.kiwee.domain.auth.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Validated
public class AuthController {

	private final JwtService jwtService;

	@GetMapping("/login/{registrationId}")
	public ResponseEntity<Void> login(
		@PathVariable String registrationId
	) {
		URI redirectUri = URI.create("/oauth2/authorization/"  + registrationId);
		return ResponseEntity.status(HttpStatus.FOUND)
			.location(redirectUri)
			.build();
	}

	@PostMapping(value = "/tokens/refresh")
	public TokenResponse refresh(
		@RequestBody @Valid RefreshTokenRequest request
	) {
		return jwtService.refresh(request.refreshToken());
	}

	@DeleteMapping(value = "/logout")
	public ResponseEntity<Void> logout(
		@AuthenticationPrincipal CustomOAuth2User principal
	) {
		jwtService.removeRefreshToken(principal.member().getId());
		return ResponseEntity.noContent().build();
	}

}
