package kr.co.jeelee.kiwee.domain.badge.dto.response;

import kr.co.jeelee.kiwee.domain.badge.entity.BadgeLevel;
import kr.co.jeelee.kiwee.domain.badge.model.BadgeGrade;

public record BadgeLevelResponse(
	String icon, String color, Integer level, BadgeGrade grade, Integer maxLevel
) {
	public static BadgeLevelResponse from(BadgeLevel badgeLevel) {
		return new BadgeLevelResponse(
			badgeLevel.getIcon(),
			badgeLevel.getColor(),
			badgeLevel.getLevel(),
			badgeLevel.getGrade(),
			badgeLevel.getRareLevel()
		);
	}
}
