package kr.co.jeelee.kiwee.domain.notification.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.domain.notification.entity.Notification;
import kr.co.jeelee.kiwee.domain.notification.model.NotificationType;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import kr.co.jeelee.kiwee.global.resolver.DomainResponseResolver;

public record NotificationResponse(
	UUID id, NotificationType type, Object publisher,
	String title, String message, List<Related> related,
	LocalDateTime readAt, LocalDateTime createdAt
) {
	public static NotificationResponse from(Notification notification, DomainObjectResolver resolver) {
		return new NotificationResponse(
			notification.getId(),
			notification.getType(),
			notification.getPublisherId() != null
				? DomainResponseResolver.toResponse(resolver.resolve(notification.getType().getPublisherType(), notification.getPublisherId()))
				: null,
			notification.getTitle(),
			notification.getMessage(),
			notification.getMetadata() != null && notification.getMetadata().relatedItems() != null
			? notification.getMetadata().relatedItems().stream()
				.map(r -> new Related(
						r.type(),
						DomainResponseResolver.toResponse(resolver.resolve(r.type(), r.id()))
					)
				).toList()
			: null,
			notification.getReadAt(),
			notification.getCreatedAt()
		);
	}

	private record Related(
		DomainType relatedType,
		Object related
	) {}
}
