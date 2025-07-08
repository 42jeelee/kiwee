package kr.co.jeelee.kiwee.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.jeelee.kiwee.global.exception.common.CastErrorException;
import kr.co.jeelee.kiwee.global.exception.common.JsonSerializationException;

public class JsonUtil {
	private static final ObjectMapper mapper = new ObjectMapper();

	public static String toJson(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new JsonSerializationException();
		}
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return mapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			throw new CastErrorException(clazz);
		}
	}

}
