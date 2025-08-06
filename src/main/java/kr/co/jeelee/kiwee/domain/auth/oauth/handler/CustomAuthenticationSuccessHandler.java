package kr.co.jeelee.kiwee.domain.auth.oauth.handler;

import java.io.IOException;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.jeelee.kiwee.domain.auth.dto.response.TokenResponse;
import kr.co.jeelee.kiwee.domain.auth.service.JwtService;
import kr.co.jeelee.kiwee.global.dto.response.common.GlobalResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final ObjectMapper objectMapper;

	private final JwtService jwtService;

	@Value("${frontend.base-url}")
	private String frontendUrl;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException, ServletException {
		TokenResponse tokenResponse= jwtService.createToken(authentication);

		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");

			objectMapper.writeValue(response.getWriter(), GlobalResponse.ok(tokenResponse));
			return;
		}

		ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokenResponse.refreshToken())
			.httpOnly(true)
			.secure(true)
			.sameSite("None")
			.path("/")
			.maxAge(Duration.ofDays(7))
			.build();
		response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

		String html = """
				<!DOCTYPE html>
				<html>
				<head><meta charset="UTF-8"><title>OAuth Success</title></head>
				<body>
				  <script>
				    window.opener.postMessage({
				      accessToken: '%s'
				    }, '%s');
				    window.close();
				  </script>
				</body>
				</html>
			""".formatted(tokenResponse.accessToken(), frontendUrl);

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.TEXT_HTML_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(html);
	}
}
