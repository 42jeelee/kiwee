package kr.co.jeelee.kiwee.domain.badge.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.badge.dto.request.BadgeCreateRequest;
import kr.co.jeelee.kiwee.domain.badge.dto.request.BadgeUpdateRequest;
import kr.co.jeelee.kiwee.domain.badge.dto.response.BadgeDetailResponse;
import kr.co.jeelee.kiwee.domain.badge.dto.response.BadgeSimpleResponse;
import kr.co.jeelee.kiwee.domain.badge.entity.Badge;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface BadgeService {

	BadgeDetailResponse createBadge(BadgeCreateRequest request);

	BadgeDetailResponse badgeDetail(UUID id);

	PagedResponse<BadgeSimpleResponse> allPublicBadges(int lv, Pageable pageable);

	BadgeDetailResponse updateBadge(UUID badgeId, BadgeUpdateRequest request);

	void deleteBadge(UUID badgeId);

	Badge getById(UUID id);

}
