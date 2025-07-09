package kr.co.jeelee.kiwee.domain.notification.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.Type;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.notification.metadata.NotificationMetadata;
import kr.co.jeelee.kiwee.domain.notification.model.NotificationType;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id", nullable = false)
	private Member receiver;

	@Column(nullable = false)
	private NotificationType type;

	@Column
	private UUID publisherId;

	@Column(length = 100, nullable = false)
	private String title;

	@Column(nullable = false)
	private String message;

	@Type(JsonBinaryType.class)
	@Column(columnDefinition = "jsonb")
	private NotificationMetadata metadata;

	@Column
	private LocalDateTime readAt;

	private Notification(
		Member receiver, NotificationType type, UUID publisherId,
		String title, String message, NotificationMetadata metadata
	) {
		this.receiver = receiver;
		this.type = type;
		this.publisherId = publisherId;
		this.title = title;
		this.message = message;
		this.metadata = metadata;
		this.readAt = null;
	}

	public static Notification of(
		Member receiver, NotificationType type,  UUID publisherId,
		String title, String message, NotificationMetadata metadata
	) {
		return new Notification(
			receiver, type, publisherId, title, message, metadata
		);
	}

	public boolean isRead() {
		return this.readAt != null;
	}

	public void read() {
		this.readAt = LocalDateTime.now();
	}

	public void unread() {
		this.readAt = null;
	}

}
