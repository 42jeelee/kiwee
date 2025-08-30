package kr.co.jeelee.kiwee.realtime.sse.event;

import java.util.Map;
import java.util.UUID;

public record SseEvent(
	UUID authorId, String type, String message,
	Map<String, Object> data
) {
}
