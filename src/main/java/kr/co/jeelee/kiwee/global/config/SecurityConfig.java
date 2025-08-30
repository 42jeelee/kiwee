package kr.co.jeelee.kiwee.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import kr.co.jeelee.kiwee.domain.auth.converter.CustomJwtAuthenticationConverter;
import kr.co.jeelee.kiwee.domain.auth.oauth.handler.CustomAuthenticationEntryPoint;
import kr.co.jeelee.kiwee.domain.auth.oauth.handler.CustomAuthenticationSuccessHandler;
import kr.co.jeelee.kiwee.domain.auth.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	private final CustomJwtAuthenticationConverter customJwtAuthenticationConverter;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.cors(cors -> cors.configurationSource(request -> {
				CorsConfiguration c = new CorsConfiguration();
				c.setAllowedOriginPatterns(List.of("*"));
				c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
				c.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
				c.setAllowCredentials(true);
				return c;
			}))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.GET, "/api/v1/platforms", "/api/v1/login/**", "/sse/stream").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/v1/tokens/refresh").permitAll()
				.anyRequest().authenticated()
			)
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint(customAuthenticationEntryPoint)
			)
			.headers(headers -> headers
				.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
			)
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userInfo -> userInfo
					.userService(customOAuth2UserService))
				.successHandler(customAuthenticationSuccessHandler)
			)
			.oauth2ResourceServer(oauth2 -> oauth2
				.jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter))
			)
		;
		return http.build();
	}
}
