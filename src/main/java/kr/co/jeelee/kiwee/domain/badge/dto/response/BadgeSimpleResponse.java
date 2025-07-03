package kr.co.jeelee.kiwee.domain.badge.dto.response;

import java.util.Comparator;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.badge.entity.Badge;
import kr.co.jeelee.kiwee.domain.badge.entity.BadgeLevel;
import kr.co.jeelee.kiwee.domain.badge.exception.BadgeLevelNotFoundException;
import kr.co.jeelee.kiwee.domain.badge.model.BadgeGrade;

public record BadgeSimpleResponse(
	UUID id, String icon, Integer level, String name,
	String color, String mainColor, String subColor,
	BadgeGrade grade, Integer rareLevel, Integer exp
) {
	public static BadgeSimpleResponse from(Badge badge, Integer lv) {

		BadgeLevel level = badge.getLevels().stream()
			.filter(l -> l.getLevel() <= lv)
			.max(Comparator.comparingInt(BadgeLevel::getLevel))
			.orElseThrow(BadgeLevelNotFoundException::new);

		return new BadgeSimpleResponse(
			badge.getId(),
			level.getIcon(),
			lv,
			badge.getName(),
			level.getColor(),
			badge.getMainColor(),
			badge.getSubColor(),
			level.getGrade(),
			level.getRareLevel(),
			badge.getExp() * lv
		);
	}
}
