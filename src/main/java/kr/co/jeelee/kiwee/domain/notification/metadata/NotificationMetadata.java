package kr.co.jeelee.kiwee.domain.notification.metadata;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;

public record NotificationMetadata(
	List<RelatedItem> relatedItems
) {
	public static NotificationMetadata of(
		List<RelatedItem> relatedItems
	) {
		return new NotificationMetadata(relatedItems);
	}

	public record RelatedItem(
		@NotNull(message = "type can't be Null.") DomainType type,
		@NotNull(message = "id can't be Null.") UUID id
	) {
		public static RelatedItem of(
			DomainType type,
			UUID id
		) {
			return new RelatedItem(type, id);
		}
	}
}
