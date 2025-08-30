package kr.co.jeelee.kiwee.realtime.sse.dto;

public record SsePayload(
	String type, String message, Object data
) {
	public static SsePayload of(String type, String message, Object data) {
		return new SsePayload(type, message, data);
	}
}
