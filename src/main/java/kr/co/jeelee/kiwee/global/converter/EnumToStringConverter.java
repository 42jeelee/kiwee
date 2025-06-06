package kr.co.jeelee.kiwee.global.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

@Converter
@RequiredArgsConstructor
public class EnumToStringConverter<T extends Enum<T>> implements AttributeConverter<T, String> {

	private final Class<T> enumClass;

	@Override
	public String convertToDatabaseColumn(T t) {
		return t != null
			? t.name().toUpperCase()
			: null;
	}

	@Override
	public T convertToEntityAttribute(String s) {
		return s != null
			? Enum.valueOf(enumClass, s.toUpperCase())
			: null;
	}
}
