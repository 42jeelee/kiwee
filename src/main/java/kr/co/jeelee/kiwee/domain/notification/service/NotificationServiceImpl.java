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
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import kr.co.jeelee.kiwee.global.resolver.DomainResponseResolver;
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
			n -> {
				Object related = domainObjectResolver.resolve(n.getRelatedDomain(), n.getRelatedId());

				return NotificationResponse.from(n, DomainResponseResolver.toResponse(related));
			}
		);
	}

	@Override
	public PagedResponse<NotificationResponse> getUnReadNotifications(UUID receiverId, Pageable pageable) {
		return PagedResponse.of(
			notificationRepository.findByReceiverIdAndIsReadFalse(receiverId, pageable),
			n -> {
				Object related = domainObjectResolver.resolve(n.getRelatedDomain(), n.getRelatedId());

				return NotificationResponse.from(n, DomainResponseResolver.toResponse(related));
			}
		);
	}

	@Override
	@Transactional
	public void readNotification(UUID receiverId, UUID notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(NotificationNotFoundException::new);

		if (!notification.getReceiver().getId().equals(receiverId)) {
			throw new AccessDeniedException("본인 알림만 읽을 수 있습니다.");
		}

		notification.read();
		notificationRepository.save(notification);
	}

	@Override
	@Transactional
	public void unReadNotification(UUID receiverId, UUID notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(NotificationNotFoundException::new);

		if (!notification.getReceiver().getId().equals(receiverId)) {
			throw new AccessDeniedException("본인 알림만 읽을 수 있습니다.");
		}

		notification.unread();
		notificationRepository.save(notification);
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
