package kr.co.jeelee.kiwee.domain.notification.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.notification.dto.response.NotificationResponse;
import kr.co.jeelee.kiwee.domain.notification.entity.Notification;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;

public interface NotificationService {

	void send(Notification notification);

	PagedResponse<NotificationResponse> getNotifications(UUID receiverId, Pageable pageable);

	PagedResponse<NotificationResponse> getUnReadNotifications(UUID receiverId, Pageable pageable);

	NotificationResponse readNotification(UUID receiverId, UUID notificationId, boolean read);

	void deleteNotification(UUID receiverId, UUID notificationId);

	void deleteOldNotifications();

}
