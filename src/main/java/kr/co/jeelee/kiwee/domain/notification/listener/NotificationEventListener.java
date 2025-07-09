package kr.co.jeelee.kiwee.domain.notification.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.notification.entity.Notification;
import kr.co.jeelee.kiwee.domain.notification.event.NotificationEvent;
import kr.co.jeelee.kiwee.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

	private final MemberService memberService;
	private final NotificationService notificationService;

	@Async
	@EventListener
	public void handle(NotificationEvent event) {
		Member receiver = memberService.getById(event.receiverId());

		Notification notification = Notification.of(
			receiver,
			event.type(),
			event.publisherId(),
			event.title(),
			event.message(),
			event.metadata()
		);

		notificationService.send(notification);
	}

}
