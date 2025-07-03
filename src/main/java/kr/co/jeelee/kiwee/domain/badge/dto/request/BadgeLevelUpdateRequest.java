package kr.co.jeelee.kiwee.domain.badge.dto.request;

import org.hibernate.validator.constraints.URL;

import kr.co.jeelee.kiwee.domain.badge.model.BadgeGrade;

public record BadgeLevelUpdateRequest(
	@URL(message = "This is not a URL format.") String icon,
	String color, Integer level, BadgeGrade grade, Integer rareLevel
) {
}
