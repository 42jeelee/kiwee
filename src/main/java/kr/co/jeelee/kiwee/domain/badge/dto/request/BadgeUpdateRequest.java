package kr.co.jeelee.kiwee.domain.badge.dto.request;

import jakarta.validation.constraints.Min;

public record BadgeUpdateRequest(
	String name, String description, String mainColor, String subColor,
	@Min(1) Integer maxLevel, @Min(0) Integer exp, Boolean isPublic
) {
}
