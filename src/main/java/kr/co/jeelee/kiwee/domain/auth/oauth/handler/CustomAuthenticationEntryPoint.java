package kr.co.jeelee.kiwee.domain.auth.oauth.handler;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.jeelee.kiwee.global.dto.response.ErrorResponse;
import kr.co.jeelee.kiwee.global.dto.response.GlobalResponse;
import kr.co.jeelee.kiwee.global.exception.custom.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
		ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), null);
		GlobalResponse<ErrorResponse> globalResponse = GlobalResponse.error(errorResponse);

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(objectMapper.writeValueAsString(globalResponse));
	}
}
