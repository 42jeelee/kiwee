package kr.co.jeelee.kiwee.realtime.sse.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class StreamTokenService {
	private final Map<String, UUID> tokenToMember = new ConcurrentHashMap<>();
	private final Map<String, Long> tokenExp = new ConcurrentHashMap<>();
	private final long ttlMs = 2 * 60 * 1000;

	public String issue(UUID memberId) {
		String token = UUID.randomUUID().toString();
		tokenToMember.put(token, memberId);
		tokenExp.put(token, System.currentTimeMillis() + ttlMs);
		return token;
	}

	public UUID validateAndConsume(String token) {
		Long exp = tokenExp.get(token);
		if (exp == null || exp < System.currentTimeMillis()) throw new RuntimeException("expired");
		UUID memberId = tokenToMember.remove(token);
		tokenExp.remove(token);
		if (memberId == null) throw new RuntimeException("invalid");
		return memberId;
	}

}
