package kr.co.jeelee.kiwee.global.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

	@Value("${jwt.secret}")
	private String secret;

	public static String MAIN_KEY_ID = "jwt-key";

	@PostConstruct
	public void validate() {
		if (secret == null || secret.length() < 32) {
			throw new IllegalArgumentException("JWT secret length should be at least 32 characters");
		}
	}

	@Bean
	public JwtEncoder jwtEncoder() {
		SecretKey secretKey = new SecretKeySpec(
			secret.getBytes(),
			"HmacSHA256"
		);

		JWK jwk = new OctetSequenceKey.Builder(secretKey)
			.keyID(MAIN_KEY_ID)
			.build();
		JWKSource<SecurityContext> jwkSource = (selector, context) -> selector.select(new JWKSet(jwk));

		return new NimbusJwtEncoder(jwkSource);
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		SecretKey secretKey = new SecretKeySpec(
			secret.getBytes(),
			"HmacSHA256"
		);
		return NimbusJwtDecoder.withSecretKey(secretKey).build();
	}

}
