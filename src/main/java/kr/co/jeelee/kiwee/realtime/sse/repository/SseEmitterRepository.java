package kr.co.jeelee.kiwee.realtime.sse.repository;

import java.util.Map;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterRepository {

	SseEmitter save(String key, SseEmitter emitter);

	void remove(String key);

	Map<String, SseEmitter> findAllStartsWith(String prefix);

}
