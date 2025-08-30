package kr.co.jeelee.kiwee.realtime.sse.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kr.co.jeelee.kiwee.realtime.sse.event.SseEvent;
import kr.co.jeelee.kiwee.realtime.sse.service.SseService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SseListener {

	private final SseService sseService;

	@EventListener
	public void handle(SseEvent e) {
		sseService.sendToMember(e.authorId(), e.type(), e.message(), e.data());
	}

}
