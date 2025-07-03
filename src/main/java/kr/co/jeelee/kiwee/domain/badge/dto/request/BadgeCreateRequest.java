package kr.co.jeelee.kiwee.domain.badge.dto.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.domain.badge.model.BadgeGrade;

public record BadgeCreateRequest(
	@NotBlank(message = "name can't be Blank.") String name,
	@NotBlank(message = "description can't be Blank.") String description,
	@NotBlank(message = "mainColor can't be Blank.") String mainColor,
	String subColor, Integer maxLevel, Integer exp,
	@URL(message = "This is not a URL format.")
	@NotNull(message = "icon can't be Null.")
	String icon,
	@NotBlank(message = "color can't be Blank.") String color,
	@NotNull(message = "grade can't be Null.") BadgeGrade grade,
	Integer rareLevel, Boolean isPublic
) {
}
