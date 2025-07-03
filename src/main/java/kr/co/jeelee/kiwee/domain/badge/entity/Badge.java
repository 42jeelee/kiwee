package kr.co.jeelee.kiwee.domain.badge.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "badges")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Badge extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private String mainColor;

	@Column(nullable = false)
	private String subColor;

	@Column(nullable = false)
	private Integer maxLevel;

	@Column(nullable = false)
	private Integer exp;

	@Column(nullable = false)
	private Boolean isPublic;

	@OneToMany(mappedBy = "badge", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BadgeLevel> levels;

	private Badge(
		String name, String description, String mainColor, String subColor,
		Integer maxLevel, Integer exp, Boolean isPublic
	) {
		this.name = name;
		this.description = description;
		this.mainColor = mainColor;
		this.subColor = subColor;
		this.maxLevel = maxLevel;
		this.exp = exp;
		this.isPublic = isPublic;
		this.levels = new ArrayList<>();
	}

	public static Badge of(
		String name, String description, String mainColor, String subColor,
		Integer maxLevel, Integer exp, Boolean isPublic
	) {
		return new Badge(name, description, mainColor, subColor, maxLevel, exp, isPublic);
	}

	public void addLevel(BadgeLevel level) {
		this.levels.add(level);
	}

	public void updateName(String name) {
		if (name == null || name.isBlank()) {
			throw new FieldValidationException("name", "이름은 비어있을 수 없습니다.");
		}
		if (name.length() > 50) {
			throw new FieldValidationException("name", "이름은 최대 50자까지 가능합니다.");
		}
		this.name = name;
	}

	public void updateDescription(String description) {
		if (description == null || description.isBlank()) {
			throw new FieldValidationException("description", "설명은 비어있을 수 없습니다.");
		}
		if (description.length() > 200) {
			throw new FieldValidationException("description", "설명은 최대 200자까지 가능합니다.");
		}
		this.description = description;
	}

	public void updateMainColor(String mainColor) {
		validateColor("mainColor", mainColor);
		this.mainColor = mainColor;
	}

	public void updateSubColor(String subColor) {
		validateColor("subColor", subColor);
		this.subColor = subColor;
	}

	public void updateMaxLevel(Integer maxLevel) {
		if (maxLevel == null || maxLevel < 0) {
			throw new FieldValidationException("maxLevel", "최대 레벨은 음수 일 수 없습니다.");
		}
		this.maxLevel = maxLevel;
	}

	public void updateExp(Integer exp) {
		if (exp == null || exp < 0) {
			throw new FieldValidationException("exp", "획득 경험치는 음수 일 수 없습니다.");
		}
		this.exp = exp;
	}

	public void isPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	private void validateColor(String columnName, String color) {
		final Pattern COLOR_PATTERN = Pattern.compile(
			"^[0-9a-fA-F]{6}([0-9a-fA-F]{2})?$"
		);

		if (color == null || color.isBlank()) {
			throw new FieldValidationException(columnName, "색상은 비어있을 수 없습니다.");
		}
		if (!COLOR_PATTERN.matcher(color).matches()) {
			throw new FieldValidationException(columnName, "색상 형식이 잘못되었습니다.");
		}
	}

}
