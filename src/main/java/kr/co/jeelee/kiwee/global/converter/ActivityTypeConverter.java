package kr.co.jeelee.kiwee.global.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.converter.common.EnumToStringConverter;

@Converter(autoApply = true)
public class ActivityTypeConverter extends EnumToStringConverter<ActivityType> {

	public ActivityTypeConverter() {
		super(ActivityType.class);
	}
}
