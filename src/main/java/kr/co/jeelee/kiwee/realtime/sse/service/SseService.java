package kr.co.jeelee.kiwee.realtime.sse.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import kr.co.jeelee.kiwee.realtime.sse.dto.SsePayload;
import kr.co.jeelee.kiwee.realtime.sse.repository.SseEmitterRepository;
import kr.co.jeelee.kiwee.realtime.sse.util.Ids;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SseService {

	private static final long TIMEOUT = 60L * 60 * 1000;
	private final SseEmitterRepository emitterRepository;

	public SseEmitter subscribe(UUID memberId) {
		String key = "member:%s:%s".formatted(memberId, UUID.randomUUID());
		SseEmitter emitter = new SseEmitter(TIMEOUT);
		emitterRepository.save(key, emitter);

		sendToKey(key, "connected", "ok", null);
		return emitter;
	}

	public void sendToMember(UUID memberId, String type, String message, Object data) {
		emitterRepository.findAllStartsWith("member:%s:".formatted(memberId))
			.forEach((key, emitter) -> send(emitter, type, message, data));
	}

	public void sendToAll(String type, String message, Object data) {
		emitterRepository.findAllStartsWith("member:")
			.forEach((key, emitter) -> send(emitter, type, message, data));
	}

	private void sendToKey(String key, String type, String message, Object data) {
		SseEmitter emitter = emitterRepository.findAllStartsWith(key).get(key);
		if (emitter != null) send(emitter, type, message, data);
	}

	private void send(SseEmitter emitter, String type, String message, Object data) {
		try {
			SsePayload payload = SsePayload.of(type, message, data);
			emitter.send(SseEmitter.event()
				.id(Ids.eventId())
				.name(type)
				.data(payload)
			);
		} catch (Exception e) {
			emitter.completeWithError(e);
		}
	}
}
