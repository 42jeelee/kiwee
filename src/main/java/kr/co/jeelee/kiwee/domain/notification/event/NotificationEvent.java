package kr.co.jeelee.kiwee.domain.notification.event;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.notification.model.NotificationType;

public record NotificationEvent(
	UUID receiverId, NotificationType type, String title,
	String message, DomainType domain, UUID relatedId
) {
	public static NotificationEvent of(
		UUID receiverId, NotificationType type, String title,
		String message, DomainType domain, UUID relatedId
	) {
		return new NotificationEvent(receiverId, type, title, message, domain, relatedId);
	}
}
