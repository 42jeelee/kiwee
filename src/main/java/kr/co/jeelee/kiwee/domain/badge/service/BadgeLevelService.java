package kr.co.jeelee.kiwee.domain.badge.service;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.badge.dto.request.BadgeLevelCreateRequest;
import kr.co.jeelee.kiwee.domain.badge.dto.request.BadgeLevelUpdateRequest;
import kr.co.jeelee.kiwee.domain.badge.dto.response.BadgeLevelResponse;

public interface BadgeLevelService {

	BadgeLevelResponse addLevel(UUID badgeId, BadgeLevelCreateRequest request);

	BadgeLevelResponse updateLevel(UUID badgeId, Long id, BadgeLevelUpdateRequest request);

	void deleteLevel(UUID badgeId, Long id);

}
