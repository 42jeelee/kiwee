package kr.co.jeelee.kiwee.global.converter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import kr.co.jeelee.kiwee.global.exception.common.CastErrorException;
import lombok.RequiredArgsConstructor;

@Converter
@RequiredArgsConstructor
public class IntegerListToStringConverter implements AttributeConverter<List<Integer>, String> {

	private final ObjectMapper objectMapper;

	@Override
	public String convertToDatabaseColumn(List<Integer> integers) {
		return integers.toString();
	}

	@Override
	public List<Integer> convertToEntityAttribute(String s) {
		if (s == null || s.trim().isEmpty()) {
			return null;
		}

		try {
			return objectMapper.readValue(s, new TypeReference<List<Integer>>() {});
		} catch (Exception e) {
			throw new CastErrorException(ArrayList.class);
		}
	}
}
