package kr.co.jeelee.kiwee.global.converter;

import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.global.converter.common.EnumToStringConverter;
import kr.co.jeelee.kiwee.global.model.TermType;

@Converter(autoApply = true)
public class TermTypeConverter extends EnumToStringConverter<TermType> {

	public TermTypeConverter() {
		super(TermType.class);
	}
}
