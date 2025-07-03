package kr.co.jeelee.kiwee.domain.badge.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.domain.badge.model.BadgeGrade;
import kr.co.jeelee.kiwee.global.converter.EnumToStringConverter;

@Converter(autoApply = true)
public class BadgeGradeConverter extends EnumToStringConverter<BadgeGrade> {

	public BadgeGradeConverter() {
		super(BadgeGrade.class);
	}
}
