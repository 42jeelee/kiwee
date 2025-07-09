package kr.co.jeelee.kiwee.domain.notification.event;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.notification.metadata.NotificationMetadata;
import kr.co.jeelee.kiwee.domain.notification.model.NotificationType;

public record NotificationEvent(
	UUID receiverId, NotificationType type, UUID publisherId,
	String title, String message, NotificationMetadata metadata
) {
	public static NotificationEvent of(
		UUID receiverId, NotificationType type, UUID publisherId,
		String title, String message, NotificationMetadata metadata
	) {
		return new NotificationEvent(receiverId, type, publisherId, title, message, metadata);
	}
}
