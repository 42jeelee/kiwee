package kr.co.jeelee.kiwee.global.util;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NicknameCreator {

	private final ObjectMapper objectMapper;

	private List<String> adjectives;
	private List<String> animals;

	@PostConstruct
	public void loadWords() {
		String NICKNAME_JSON_PATH = "/nickname/nickname-ko.json";

		try (InputStream is = new ClassPathResource(NICKNAME_JSON_PATH).getInputStream()) {
			JsonNode root = objectMapper.readTree(is);

			adjectives = objectMapper.convertValue(root.get("adjectives"), new TypeReference<List<String>>() {});
			animals = objectMapper.convertValue(root.get("animals"), new TypeReference<List<String>>() {});
		} catch (Exception e) {
			throw new IllegalStateException("닉네임 단어 불러오기 에러", e);
		}
	}

	public String createNickname() {
		String adjective = adjectives.get(ThreadLocalRandom.current().nextInt(adjectives.size()));
		String animal = animals.get(ThreadLocalRandom.current().nextInt(animals.size()));
		String number = String.format("%04d", ThreadLocalRandom.current().nextInt(10000));

		return adjective + animal + number;
	}

}
