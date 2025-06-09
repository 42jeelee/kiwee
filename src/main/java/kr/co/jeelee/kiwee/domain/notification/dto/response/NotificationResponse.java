package kr.co.jeelee.kiwee.domain.notification.dto.response;

import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.notification.entity.Notification;
import kr.co.jeelee.kiwee.domain.notification.model.NotificationType;

public record NotificationResponse(
	MemberSimpleResponse receiver, NotificationType type, String title,
	String message, Object related, Boolean isRead
) {
	public static NotificationResponse from(Notification notification, Object related) {
		return new NotificationResponse(
			MemberSimpleResponse.from(notification.getReceiver()),
			notification.getType(),
			notification.getTitle(),
			notification.getMessage(),
			related,
			notification.getIsRead()
		);
	}
}
