package kr.co.jeelee.kiwee.domain.memberActivity.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.memberActivity.model.ActivityType;
import kr.co.jeelee.kiwee.global.converter.EnumToStringConverter;

@Converter(autoApply = true)
public class ActivityTypeConverter extends EnumToStringConverter<ActivityType> {

	public ActivityTypeConverter() {
		super(ActivityType.class);
	}
}
