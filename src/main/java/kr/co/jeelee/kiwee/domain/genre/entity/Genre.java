package kr.co.jeelee.kiwee.domain.genre.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "genres")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Genre {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;

	private Genre(String name) {
		this.name = name;
	}

	public static Genre of(String name) {
		return new Genre(name);
	}

	public void changeName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new FieldValidationException("name", "이름은 비어있을 수 없습니다.");
		}
		this.name = name;
	}
}
