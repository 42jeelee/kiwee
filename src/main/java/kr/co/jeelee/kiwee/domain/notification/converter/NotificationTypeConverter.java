package kr.co.jeelee.kiwee.domain.notification.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.notification.model.NotificationType;
import kr.co.jeelee.kiwee.global.converter.common.EnumToStringConverter;

@Converter(autoApply = true)
public class NotificationTypeConverter extends EnumToStringConverter<NotificationType> {

	public NotificationTypeConverter() {
		super(NotificationType.class);
	}
}
