package kr.co.jeelee.kiwee.realtime.sse.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class InMemoryEmitterRepository implements SseEmitterRepository{

	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

	@Override
	public SseEmitter save(String key, SseEmitter emitter) {
		emitters.put(key, emitter);
		emitter.onCompletion(() -> emitters.remove(key));
		emitter.onTimeout(() -> emitters.remove(key));
		emitter.onError(e -> emitters.remove(key));
		return emitter;
	}

	@Override
	public void remove(String key) {
		emitters.remove(key);
	}

	@Override
	public Map<String, SseEmitter> findAllStartsWith(String prefix) {
		return emitters.entrySet().stream()
			.filter(e -> e.getKey().startsWith(prefix))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

}
