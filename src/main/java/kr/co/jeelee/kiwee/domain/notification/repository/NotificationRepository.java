package kr.co.jeelee.kiwee.domain.notification.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

	Page<Notification> findByReceiverId(UUID receiverId, Pageable pageable);

	Page<Notification> findByReceiverIdAndReadAtIsNull(UUID receiverId, Pageable pageable);

	void deleteByCreatedAtBefore(LocalDateTime threshold);

}
