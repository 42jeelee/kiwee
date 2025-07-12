package kr.co.jeelee.kiwee.domain.platform.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.platform.dto.request.PlatformCreateRequest;
import kr.co.jeelee.kiwee.domain.platform.dto.request.PlatformUpdateRequest;
import kr.co.jeelee.kiwee.domain.platform.dto.response.PlatformDetailResponse;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.model.DataProvider;

public interface PlatformService {

	PlatformDetailResponse createPlatform(PlatformCreateRequest request);
	PlatformDetailResponse createOrGetPlatform(PlatformCreateRequest request);

	PagedResponse<PlatformDetailResponse> getAllPlatforms(Pageable pageable);
	PagedResponse<PlatformDetailResponse> searchPlatforms(String keyword, Pageable pageable);
	PlatformDetailResponse getPlatformById(UUID id);

	PlatformDetailResponse updatePlatform(UUID id, PlatformUpdateRequest request);

	void deletePlatformById(UUID id);

	Platform getEntityByProvider(String provider);

	Platform getBySourceId(DataProvider sourceProvider, String sourceId);
	Platform getById(UUID id);

}
