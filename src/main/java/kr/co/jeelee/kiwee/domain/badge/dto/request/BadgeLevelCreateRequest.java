package kr.co.jeelee.kiwee.domain.badge.dto.request;

import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.domain.badge.model.BadgeGrade;

public record BadgeLevelCreateRequest(
	@URL(message = "This is not a URL format.")
	@NotNull(message = "icon can't be Null.")
	String icon,
	@NotBlank(message = "color can't be Blank.") String color,
	@Min(value = 2, message = "level must be at least 2.")
	@NotNull(message = "level can't be Null.")
	Integer level,
	@NotNull(message = "grade can't be Null.") BadgeGrade grade,
	Integer rareLevel
) {
}
