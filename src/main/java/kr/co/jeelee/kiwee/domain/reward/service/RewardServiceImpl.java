package kr.co.jeelee.kiwee.domain.reward.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.reward.dto.request.RewardCreateRequest;
import kr.co.jeelee.kiwee.domain.reward.dto.response.RewardDetailResponse;
import kr.co.jeelee.kiwee.domain.reward.dto.response.RewardSimpleResponse;
import kr.co.jeelee.kiwee.domain.reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.reward.exception.RewardNotFoundException;
import kr.co.jeelee.kiwee.domain.reward.model.RewardType;
import kr.co.jeelee.kiwee.domain.reward.repository.RewardRepository;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.model.PermissionType;
import kr.co.jeelee.kiwee.domain.badge.entity.Badge;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.channelMember.service.ChannelMemberService;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RewardServiceImpl implements RewardService {

	private final RewardRepository rewardRepository;

	private final DomainObjectResolver domainObjectResolver;

	private final ChannelMemberService channelMemberService;

	@Override
	@Transactional
	public RewardDetailResponse createReward(CustomOAuth2User principal, RewardCreateRequest request) {
		Object source = domainObjectResolver.resolve(request.sourceType(), request.sourceId());

		if (source instanceof Channel channel) {
			if (!channelMemberService.hasPermission(channel, principal.member(), PermissionType.ROLE_CHANNEL_MAKE_REWARD)) {
				throw new AccessDeniedException("채널 보상을 만들 권한이 없습니다.");
			}
		}

		Object rewardObj = domainObjectResolver.resolve(request.rewardType().getDomainType(), request.rewardId());
		int exp = request.exp() != null? request.exp() : 0;

		if (request.rewardType().equals(RewardType.NONE) && exp <= 0) {
			throw new FieldValidationException("exp", "경험치 보상은 경험치가 있어야 합니다.");
		} else if (rewardObj instanceof Badge) {
			exp = ((Badge)rewardObj).getExp();
		}

		if (request.activityType() == ActivityType.END && request.duration() == null) {
			throw new FieldValidationException("duration", "활동 종료 보상에는 플레이 기간이 포함되어야 합니다.");
		}

		Reward reward = Reward.of(
			principal.member(),
			request.sourceType(),
			request.sourceId(),
			request.rewardType(),
			request.rewardId(),
			request.activityType(),
			request.activityCount(),
			request.duration(),
			request.title(),
			request.description(),
			exp,
			request.isPublic()
		);

		return RewardDetailResponse.from(rewardRepository.save(reward), domainObjectResolver);
	}

	@Override
	public PagedResponse<RewardSimpleResponse> getPublicRewards(Pageable pageable) {
		return PagedResponse.of(
			rewardRepository.findByIsPublicTrue(pageable),
			r -> RewardSimpleResponse.from(r, domainObjectResolver)
		);
	}

	@Override
	public PagedResponse<RewardSimpleResponse> getRewardsBySource(
		CustomOAuth2User principal,
		DomainType domainType,
		UUID domainId,
		Pageable pageable
	) {
		Object source = domainObjectResolver.resolve(domainType, domainId);

		if (source instanceof Channel) {
			if (!channelMemberService.isJoined((Channel) source, principal.member())) {
				throw new AccessDeniedException("채널에 가입해야 보상목록을 볼 수 있습니다.");
			}
		}

		return PagedResponse.of(
			rewardRepository.findBySourceTypeAndSourceIdAndIsPublicTrue(
				domainType, domainId, pageable
			),
			r -> RewardSimpleResponse.from(r, domainObjectResolver)
		);
	}

	@Override
	@Transactional
	public void deleteReward(UUID id) {
		rewardRepository.deleteById(id);
	}

	@Override
	public Reward getById(UUID id) {
		return rewardRepository.findById(id)
			.orElseThrow(RewardNotFoundException::new);
	}

	@Override
	public List<Reward> getGeneralRewards(DomainType domainType, ActivityType activityType) {
		return rewardRepository.findBySourceTypeAndSourceIdIsNullAndActivityType(domainType, activityType);
	}

	@Override
	public List<Reward> getSpecificRewards(DomainType domain, UUID domainId, ActivityType activityType) {
		return rewardRepository.findBySourceTypeAndSourceIdAndActivityType(domain, domainId, activityType);
	}

}
