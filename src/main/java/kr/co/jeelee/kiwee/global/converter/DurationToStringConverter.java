package kr.co.jeelee.kiwee.global.converter;

import java.time.Duration;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DurationToStringConverter implements AttributeConverter<Duration, String> {

	@Override
	public String convertToDatabaseColumn(Duration duration) {
		return duration != null ? duration.toString() : null;
	}

	@Override
	public Duration convertToEntityAttribute(String s) {
		return s != null ? Duration.parse(s) : null;
	}
}
