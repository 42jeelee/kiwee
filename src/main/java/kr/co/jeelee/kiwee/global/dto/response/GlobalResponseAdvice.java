package kr.co.jeelee.kiwee.global.dto.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice(basePackages = "kr.co.jeelee.kiwee")
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {
	@Override
	public boolean supports(
		@NonNull MethodParameter returnType,
		@NonNull Class<? extends HttpMessageConverter<?>> converterType
	) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(
		Object body,
		@NonNull MethodParameter returnType,
		@NonNull MediaType selectedContentType,
		@NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
		@NonNull ServerHttpRequest request,
		@NonNull ServerHttpResponse response
	) {
		HttpServletResponse httpServletResponse = ((ServletServerHttpResponse) response).getServletResponse();
		int status = httpServletResponse.getStatus();
		HttpStatus httpStatus = HttpStatus.resolve(status);

		if (httpStatus == null || body instanceof String) {
			return body;
		}

		if (httpStatus.is2xxSuccessful()) {
			return GlobalResponse.ok(body);
		}

		return body;
	}
}
