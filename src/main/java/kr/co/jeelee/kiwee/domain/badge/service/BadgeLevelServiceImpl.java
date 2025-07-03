package kr.co.jeelee.kiwee.domain.badge.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.badge.dto.request.BadgeLevelCreateRequest;
import kr.co.jeelee.kiwee.domain.badge.dto.request.BadgeLevelUpdateRequest;
import kr.co.jeelee.kiwee.domain.badge.dto.response.BadgeLevelResponse;
import kr.co.jeelee.kiwee.domain.badge.entity.Badge;
import kr.co.jeelee.kiwee.domain.badge.entity.BadgeLevel;
import kr.co.jeelee.kiwee.domain.badge.exception.BadgeLevelAlreadyExistException;
import kr.co.jeelee.kiwee.domain.badge.exception.BadgeLevelCantRemoveException;
import kr.co.jeelee.kiwee.domain.badge.exception.BadgeNotFoundException;
import kr.co.jeelee.kiwee.domain.badge.model.BadgeGrade;
import kr.co.jeelee.kiwee.domain.badge.repository.BadgeLevelRepository;
import kr.co.jeelee.kiwee.domain.badge.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BadgeLevelServiceImpl implements BadgeLevelService {

	private final BadgeRepository badgeRepository;
	private final BadgeLevelRepository badgeLevelRepository;

	@Override
	@Transactional
	public BadgeLevelResponse addLevel(UUID badgeId, BadgeLevelCreateRequest request) {
		Badge badge = badgeRepository.findById(badgeId)
			.orElseThrow(BadgeNotFoundException::new);

		if (badgeLevelRepository.existsByBadgeAndLevel(badge, request.level())) {
			throw new BadgeLevelAlreadyExistException();
		}

		BadgeLevel level = BadgeLevel.of(
			badge,
			request.icon(),
			request.color(),
			request.level(),
			request.grade(),
			request.rareLevel()
		);

		return BadgeLevelResponse.from(badgeLevelRepository.save(level));
	}

	@Override
	@Transactional
	public BadgeLevelResponse updateLevel(Long id, BadgeLevelUpdateRequest request) {
		BadgeLevel level = badgeLevelRepository.findById(id)
			.orElseThrow(BadgeNotFoundException::new);

		updateIconIfChanged(level, request.icon());
		updateColorIfChanged(level, request.color());
		updateLevelIfChanged(level, request.level());
		updateGradeIfChanged(level, request.grade());
		updateRareLevelIfChanged(level, request.rareLevel());

		return BadgeLevelResponse.from(level);
	}

	@Override
	@Transactional
	public void deleteLevel(Long id) {
		BadgeLevel level = badgeLevelRepository.findById(id)
			.orElseThrow(BadgeNotFoundException::new);

		if (level.getLevel() == 1) {
			throw new BadgeLevelCantRemoveException();
		}

		badgeLevelRepository.delete(level);
	}

	private void updateIconIfChanged(BadgeLevel level, String icon) {
		if (icon != null && !icon.equals(level.getIcon())) {
			level.updateIcon(icon);
		}
	}

	private void updateColorIfChanged(BadgeLevel level, String color) {
		if (color != null && !color.equals(level.getColor())) {
			level.updateColor(color);
		}
	}

	private void updateLevelIfChanged(BadgeLevel level, Integer lv) {
		if (lv != null && !lv.equals(level.getLevel())) {
			level.updateLevel(lv);
		}
	}

	private void updateGradeIfChanged(BadgeLevel level, BadgeGrade grade) {
		if (grade != null && !grade.equals(level.getGrade())) {
			level.updateGrade(grade);
		}
	}

	private void updateRareLevelIfChanged(BadgeLevel level, Integer rareLevel) {
		if (rareLevel != null && !rareLevel.equals(level.getRareLevel())) {
			level.updateRareLevel(rareLevel);
		}
	}

}
