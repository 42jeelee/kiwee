package kr.co.jeelee.kiwee.domain.platform.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.platform.dto.request.PlatformCreateRequest;
import kr.co.jeelee.kiwee.domain.platform.dto.request.PlatformUpdateRequest;
import kr.co.jeelee.kiwee.domain.platform.dto.response.PlatformDetailResponse;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;
import kr.co.jeelee.kiwee.domain.platform.exception.PlatformNotFoundException;
import kr.co.jeelee.kiwee.domain.platform.repository.PlatformRepository;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.model.DataProvider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlatformServiceImpl implements PlatformService {

	private final PlatformRepository platformRepository;

	@Override
	@Transactional
	public PlatformDetailResponse createPlatform(PlatformCreateRequest request) {

		if (platformRepository.existsBySourceProviderAndSourceId(request.sourceProvider(), request.sourceId())) {
			throw new FieldValidationException("sourceId", "이미 존재하는 플랫폼입니다.");
		}

		Platform platform = Platform.of(
			request.name(),
			request.icon(),
			request.banner(),
			request.sourceProvider(),
			request.sourceId(),
			request.description(),
			request.homePage(),
			request.provider(),
			request.isToken()
		);

		if (platformRepository.existsByName(platform.getName())) {
			throw new FieldValidationException("name", "이름이 중복되었습니다.");
		}

		return PlatformDetailResponse.from(platformRepository.save(platform));
	}

	@Override
	public PlatformDetailResponse createOrGetPlatform(PlatformCreateRequest request) {
		return platformRepository.findBySourceProviderAndSourceId(
			request.sourceProvider(), request.sourceId()
		)
			.map(PlatformDetailResponse::from)
			.orElseGet(() -> createPlatform(request));
	}

	@Override
	public PagedResponse<PlatformDetailResponse> getAllPlatforms(Pageable pageable) {
		return PagedResponse.of(platformRepository.findAll(pageable), PlatformDetailResponse::from);
	}

	@Override
	public PagedResponse<PlatformDetailResponse> searchPlatforms(String keyword, Pageable pageable) {
		return PagedResponse.of(
			platformRepository.findByNameContaining(keyword, pageable),
			PlatformDetailResponse::from
		);
	}

	@Override
	public PlatformDetailResponse getPlatformById(UUID id) {
		return platformRepository.findById(id)
			.map(PlatformDetailResponse::from)
			.orElseThrow(PlatformNotFoundException::new);
	}

	@Override
	@Transactional
	public PlatformDetailResponse updatePlatform(UUID id, PlatformUpdateRequest request) {
		Platform platform = platformRepository.findById(id)
			.orElseThrow(PlatformNotFoundException::new);

		updateNameIfChanged(platform, request.name());
		updateIconIfChanged(platform, request.icon());
		updateBannerIfChanged(platform, request.banner());
		updateDescriptionIfChanged(platform, request.description());
		updatePageIfChanged(platform, request.page());
		updateProviderIfChanged(platform, request.provider());
		updateIsTokenIfChanged(platform, request.isToken());

		return PlatformDetailResponse.from(platform);
	}

	@Override
	@Transactional
	public void deletePlatformById(UUID id) {
		platformRepository.deleteById(id);
	}

	@Override
	public Platform getEntityByProvider(String provider) {
		return platformRepository.findByProvider(provider)
			.orElseThrow(PlatformNotFoundException::new);
	}

	@Override
	public Platform getBySourceId(DataProvider sourceProvider, String sourceId) {
		return platformRepository.findBySourceProviderAndSourceId(sourceProvider, sourceId)
			.orElseThrow(PlatformNotFoundException::new);
	}

	@Override
	public Platform getById(UUID id) {
		return platformRepository.findById(id)
			.orElseThrow(PlatformNotFoundException::new);
	}

	private void updateNameIfChanged(Platform platform, String name) {
		if (name != null && !name.equals(platform.getName())) {
			if (platformRepository.existsByName(name)) {
				throw new FieldValidationException("name", "이름이 중복되었습니다.");
			}

			platform.updateName(name);
		}
	}

	private void updateIconIfChanged(Platform platform, String icon) {
		if (icon != null && !icon.equals(platform.getIcon())) {
			platform.updateIcon(icon);
		}
	}

	private void updateBannerIfChanged(Platform platform, String banner) {
		if (banner != null && !banner.equals(platform.getBanner())) {
			platform.updateBanner(banner);
		}
	}

	private void updateDescriptionIfChanged(Platform platform, String description) {
		if (description != null && !description.equals(platform.getDescription())) {
			platform.updateDescription(description);
		}
	}

	private void updatePageIfChanged(Platform platform, String page) {
		if (page != null && !page.equals(platform.getHomePage())) {
			platform.updatePage(page);
		}
	}

	private void updateProviderIfChanged(Platform platform, String provider) {
		if (provider != null && !provider.equals(platform.getProvider())) {
			platform.updateProvider(provider);
		}
	}

	private void updateIsTokenIfChanged(Platform platform, Boolean isToken) {
		if (isToken != null && !isToken.equals(platform.isToken())) {
			platform.updateToken(isToken);
		}
	}
}
