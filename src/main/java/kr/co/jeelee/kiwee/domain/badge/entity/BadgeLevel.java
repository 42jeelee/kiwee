package kr.co.jeelee.kiwee.domain.badge.entity;

import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kr.co.jeelee.kiwee.domain.badge.model.BadgeGrade;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "badge_levels",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_badge_level", columnNames = {"badge_id", "level"})
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BadgeLevel extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "badge_id")
	private Badge badge;

	@Column(nullable = false)
	private String icon;

	@Column(nullable = false)
	private String color;

	@Column(nullable = false)
	private Integer level;

	@Column(nullable = false)
	private BadgeGrade grade;

	@Column(nullable = false)
	private Integer rareLevel;

	private BadgeLevel(
		Badge badge, String icon, String color,
		Integer level, BadgeGrade grade, Integer rareLevel
	) {
		this.badge = badge;
		this.icon = icon;
		this.color = color;
		this.level = level;
		this.grade = grade;
		this.rareLevel = rareLevel;
	}

	public static BadgeLevel of(
		Badge badge, String icon, String color,
		Integer level, BadgeGrade grade, Integer rareLevel
	) {
		return new BadgeLevel(badge, icon, color, level, grade, rareLevel);
	}

	public void updateIcon(String icon) {
		final Pattern IMAGE_PATTERN = Pattern.compile(
			"^(https?://)([\\w\\-]+\\.)+[\\w\\-]+(/[\\w\\-./?%&=]*)?\\.(jpg|jpeg|png|gif|bmp|svg)$"
		);

		if (icon == null || icon.isBlank()) {
			throw new FieldValidationException("icon", "사진 URL은 비어있을 수 없습니다.");
		}
		if (!IMAGE_PATTERN.matcher(icon).matches()) {
			throw new FieldValidationException("icon", "사진 URL 형식이 아닙니다.");
		}
		this.icon = icon;
	}

	public void updateColor(String color) {
		final Pattern COLOR_PATTERN = Pattern.compile(
			"^[0-9a-fA-F]{6}([0-9a-fA-F]{2})?$"
		);

		if (color == null || color.isBlank()) {
			throw new FieldValidationException("color", "색상은 비어있을 수 없습니다.");
		}
		if (!COLOR_PATTERN.matcher(color).matches()) {
			throw new FieldValidationException("color", "색상 형식이 아닙니다.");
		}
		this.color = color;
	}

	public void updateLevel(Integer level) {
		if (level == null || level <= 0) {
			throw new FieldValidationException("level", "레벨은 0이하일 수 없습니다.");
		}
		this.level = level;
	}

	public void updateGrade(BadgeGrade grade) {
		this.grade = grade;
	}

	public void updateRareLevel(Integer rareLevel) {
		if (rareLevel == null || rareLevel <= 0) {
			throw new FieldValidationException("rareLevel", "휘귀도는 0이하일 수 없습니다.");
		}
		this.rareLevel = rareLevel;
	}

}
