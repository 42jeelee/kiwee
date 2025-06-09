package kr.co.jeelee.kiwee.domain.notification.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
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

	@Column
	private NotificationType type;

	@Column(length = 100, nullable = false)
	private String title;

	@Column(nullable = false)
	private String message;

	@Column
	private DomainType relatedDomain;

	@Column
	private UUID relatedId;

	@Column(nullable = false)
	private Boolean isRead;

	private Notification(
		Member receiver, NotificationType type, String title,
		String message, DomainType relatedDomain, UUID relatedId
	) {
		this.receiver = receiver;
		this.type = type;
		this.title = title;
		this.message = message;
		this.relatedDomain = relatedDomain;
		this.relatedId = relatedId;
		this.isRead = false;
	}

	public static Notification of(
		Member receiver, NotificationType type, String title,
		String message, DomainType relatedDomain, UUID relatedId
	) {
		return new Notification(
			receiver, type, title, message, relatedDomain, relatedId
		);
	}

	public void read() {
		this.isRead = true;
	}

	public void unread() {
		this.isRead = false;
	}

}
