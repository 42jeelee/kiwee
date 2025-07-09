package kr.co.jeelee.kiwee.domain.notification.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.notification.dto.response.NotificationResponse;
import kr.co.jeelee.kiwee.domain.notification.entity.Notification;
import kr.co.jeelee.kiwee.domain.notification.exception.NotificationNotFoundException;
import kr.co.jeelee.kiwee.domain.notification.repository.NotificationRepository;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.exception.common.InvalidParameterException;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;

	private final DomainObjectResolver domainObjectResolver;

	@Override
	@Transactional
	public void send(Notification notification) {
		notificationRepository.save(notification);
	}

	@Override
	public PagedResponse<NotificationResponse> getNotifications(UUID receiverId, Pageable pageable) {
		return PagedResponse.of(
			notificationRepository.findByReceiverId(receiverId, pageable),
			n ->  NotificationResponse.from(n, domainObjectResolver)
		);
	}

	@Override
	public PagedResponse<NotificationResponse> getUnReadNotifications(UUID receiverId, Pageable pageable) {
		return PagedResponse.of(
			notificationRepository.findByReceiverIdAndReadAtIsNotNull(receiverId, pageable),
			n -> NotificationResponse.from(n, domainObjectResolver)
		);
	}

	@Override
	@Transactional
	public NotificationResponse readNotification(UUID receiverId, UUID notificationId, boolean read) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(NotificationNotFoundException::new);

		if (!notification.getReceiver().getId().equals(receiverId)) {
			throw new AccessDeniedException("본인 알림만 읽을 수 있습니다.");
		}

		if (read ^ notification.isRead()) {
			throw new InvalidParameterException("notificationId", "이미 읽거나 읽지 않은 알림입니다.");
		}

		if (read) notification.read();
		else notification.unread();
		return NotificationResponse.from(notification, domainObjectResolver);
	}

	@Override
	@Transactional
	public void deleteNotification(UUID receiverId, UUID notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(NotificationNotFoundException::new);

		if (!notification.getReceiver().getId().equals(receiverId)) {
			throw new AccessDeniedException("본인 알림만 지울 수 있습니다.");
		}

		notificationRepository.deleteById(notificationId);
	}

	@Override
	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void deleteOldNotifications() {
		LocalDateTime ninetyDaysAgo = LocalDateTime.now().minusDays(90);
		notificationRepository.deleteByCreatedAtBefore(ninetyDaysAgo);
	}

}
