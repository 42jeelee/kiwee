package kr.co.jeelee.kiwee.domain.Reward.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.Reward.dto.request.RewardCreateRequest;
import kr.co.jeelee.kiwee.domain.Reward.dto.response.RewardDetailResponse;
import kr.co.jeelee.kiwee.domain.Reward.dto.response.RewardSimpleResponse;
import kr.co.jeelee.kiwee.domain.Reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.memberActivity.model.ActivityType;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;

public interface RewardService {

	RewardDetailResponse createReward(CustomOAuth2User principal, RewardCreateRequest request);

	PagedResponse<RewardSimpleResponse> getPublicRewards(Pageable pageable);

	PagedResponse<RewardSimpleResponse> getRewardsBySource(
		CustomOAuth2User principal,
		DomainType domainType,
		UUID sourceId,
		Pageable pageable
	);

	void deleteReward(UUID id);

	Reward getById(UUID id);

	List<Reward> getGeneralRewards(DomainType domainType, ActivityType activityType);
	List<Reward> getSpecificRewards(DomainType domain, UUID domainId, ActivityType activityType);

}
