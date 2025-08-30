package kr.co.jeelee.kiwee.realtime.sse.util;

import java.util.UUID;

public final class Ids {
	private Ids() {}
	public static String eventId() {
		return UUID.randomUUID().toString();
	}
}
