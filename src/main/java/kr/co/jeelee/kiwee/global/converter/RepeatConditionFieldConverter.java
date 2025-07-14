package kr.co.jeelee.kiwee.global.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.global.converter.common.EnumToStringConverter;
import kr.co.jeelee.kiwee.global.model.RepeatConditionField;

@Converter(autoApply = true)
public class RepeatConditionFieldConverter extends EnumToStringConverter<RepeatConditionField> {

	public RepeatConditionFieldConverter() {
		super(RepeatConditionField.class);
	}
}
