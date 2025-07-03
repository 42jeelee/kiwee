package kr.co.jeelee.kiwee.domain.badge.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.badge.dto.request.BadgeCreateRequest;
import kr.co.jeelee.kiwee.domain.badge.dto.request.BadgeUpdateRequest;
import kr.co.jeelee.kiwee.domain.badge.dto.response.BadgeDetailResponse;
import kr.co.jeelee.kiwee.domain.badge.dto.response.BadgeSimpleResponse;
import kr.co.jeelee.kiwee.domain.badge.entity.Badge;
import kr.co.jeelee.kiwee.domain.badge.entity.BadgeLevel;
import kr.co.jeelee.kiwee.domain.badge.exception.BadgeNotFoundException;
import kr.co.jeelee.kiwee.domain.badge.repository.BadgeRepository;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BadgeServiceImpl implements BadgeService {

	private final BadgeRepository badgeRepository;

	@Override
	@Transactional
	public BadgeDetailResponse createBadge(BadgeCreateRequest request) {

		Badge badge = Badge.of(
			request.name(),
			request.description(),
			request.mainColor(),
			request.subColor() != null ? request.subColor() : request.mainColor(),
			request.maxLevel() != null? request.maxLevel() : 0,
			request.exp() != null ? request.exp() : 0,
			request.isPublic() != null? request.isPublic() : true
		);
		BadgeLevel level = BadgeLevel.of(
			badge,
			request.icon(),
			request.color(),
			1,
			request.grade(),
			request.rareLevel() != null? request.rareLevel() : 0
		);

		badge.addLevel(level);

		Badge savedBadge = badgeRepository.save(badge);

		return BadgeDetailResponse.from(savedBadge);
	}

	@Override
	public BadgeDetailResponse badgeDetail(UUID id) {
		return badgeRepository.findById(id)
			.map(BadgeDetailResponse::from)
			.orElseThrow(BadgeNotFoundException::new);
	}

	@Override
	public PagedResponse<BadgeSimpleResponse> allPublicBadges(int lv, Pageable pageable) {
		return PagedResponse.of(
			badgeRepository.findByIsPublicTrue(pageable),
			b -> BadgeSimpleResponse.from(b, lv)
		);
	}

	@Override
	@Transactional
	public BadgeDetailResponse updateBadge(UUID badgeId, BadgeUpdateRequest request) {
		Badge badge = getById(badgeId);

		updateNameIfChanged(badge, request.name());
		updateDescriptionIfChanged(badge, request.description());
		updateMainColorIfChanged(badge, request.mainColor());
		updateSubColorIfChanged(badge, request.subColor());
		updateExpIfChanged(badge, request.exp());
		updateIsPublicIfChanged(badge, request.isPublic());

		return BadgeDetailResponse.from(badge);
	}

	@Override
	@Transactional
	public void deleteBadge(UUID badgeId) {
		badgeRepository.deleteById(badgeId);
	}

	@Override
	public Badge getById(UUID id) {
		return badgeRepository.findById(id)
			.orElseThrow(BadgeNotFoundException::new);
	}

	private void updateNameIfChanged(Badge badge, String name) {
		if (name != null && !name.equals(badge.getName())) {
			badge.updateName(name);
		}
	}

	private void updateDescriptionIfChanged(Badge badge, String description) {
		if (description != null && !description.equals(badge.getDescription())) {
			badge.updateDescription(description);
		}
	}

	private void updateMainColorIfChanged(Badge badge, String mainColor) {
		if (mainColor != null && !mainColor.equals(badge.getMainColor())) {
			badge.updateMainColor(mainColor);
		}
	}

	private void updateSubColorIfChanged(Badge badge, String subColor) {
		if (subColor != null && !subColor.equals(badge.getSubColor())) {
			badge.updateSubColor(subColor);
		}
	}

	private void updateMaxLevelIfChanged(Badge badge, Integer maxLevel) {
		if (maxLevel != null && !maxLevel.equals(badge.getMaxLevel())) {
			badge.updateMaxLevel(maxLevel);
		}
	}

	private void updateExpIfChanged(Badge badge, Integer exp) {
		if (exp != null && !exp.equals(badge.getExp())) {
			badge.updateExp(exp);
		}
	}

	private void updateIsPublicIfChanged(Badge badge, Boolean isPublic) {
		if (isPublic != null && !isPublic.equals(badge.getIsPublic())) {
			badge.isPublic(isPublic);
		}
	}

}
