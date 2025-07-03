package kr.co.jeelee.kiwee.domain.badge.dto.response;

import java.util.List;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.badge.entity.Badge;

public record BadgeDetailResponse(
	UUID id, String name, String description,
	String mainColor, String subColor, Integer maxLevel,
	Integer exp, List<BadgeLevelResponse> badgeLevels
) {
	public static BadgeDetailResponse from(Badge badge) {
		return new BadgeDetailResponse(
			badge.getId(),
			badge.getName(),
			badge.getDescription(),
			badge.getMainColor(),
			badge.getSubColor(),
			badge.getMaxLevel(),
			badge.getExp(),
			badge.getLevels().stream()
				.map(BadgeLevelResponse::from)
				.toList()
		);
	}
}
