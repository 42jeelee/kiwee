package kr.co.jeelee.kiwee.global.converter;

import java.time.YearMonth;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class YearMonthAttributeConverter implements AttributeConverter<YearMonth, String> {

	@Override
	public String convertToDatabaseColumn(YearMonth yearMonth) {
		return yearMonth != null
			? yearMonth.toString()
			: null
		;
	}

	@Override
	public YearMonth convertToEntityAttribute(String s) {
		return s != null
			? YearMonth.parse(s)
			: null
		;
	}

}
