package kr.co.jeelee.kiwee.domain.Reward.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.Reward.dto.request.RewardCreateRequest;
import kr.co.jeelee.kiwee.domain.Reward.dto.response.RewardDetailResponse;
import kr.co.jeelee.kiwee.domain.Reward.dto.response.RewardSimpleResponse;
import kr.co.jeelee.kiwee.domain.Reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.Reward.exception.RewardNotFoundException;
import kr.co.jeelee.kiwee.domain.Reward.model.RewardType;
import kr.co.jeelee.kiwee.domain.Reward.model.TriggerType;
import kr.co.jeelee.kiwee.domain.Reward.repository.RewardRepository;
import kr.co.jeelee.kiwee.domain.Reward.resolver.RewardObjectResolver;
import kr.co.jeelee.kiwee.domain.Reward.resolver.RewardResponseResolver;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.authorization.model.PermissionType;
import kr.co.jeelee.kiwee.domain.badge.entity.Badge;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.channelMember.service.ChannelMemberService;
import kr.co.jeelee.kiwee.domain.quest.entity.Quest;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import kr.co.jeelee.kiwee.global.resolver.DomainResponseResolver;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RewardServiceImpl implements RewardService {

	private final RewardRepository rewardRepository;

	private final DomainObjectResolver domainObjectResolver;
	private final RewardObjectResolver rewardObjectResolver;

	private final ChannelMemberService channelMemberService;

	@Override
	@Transactional
	public RewardDetailResponse createReward(CustomOAuth2User principal, RewardCreateRequest request) {
		Object source = domainObjectResolver.resolve(request.sourceType(), request.sourceId());

		if (source instanceof Channel) {
			if (!channelMemberService.hasPermission((Channel) source, principal.member(), PermissionType.ROLE_CHANNEL_MAKE_REWARD)) {
				throw new AccessDeniedException("채널 보상을 만들 권한이 없습니다.");
			}
		} else if (source instanceof Quest) {
			if (!((Quest) source).getProposer().getId().equals(principal.member().getId())) {
				throw new AccessDeniedException("퀘스트 제작자만 등록할 수 있습니다.");
			}
		}

		Object rewardObj = rewardObjectResolver.resolve(request.rewardType(), request.rewardId());
		int exp = request.exp() != null? request.exp() : 0;

		if (request.rewardType().equals(RewardType.NONE) && exp <= 0) {
			throw new FieldValidationException("exp", "경험치 보상은 경험치가 있어야 합니다.");
		} else if (rewardObj instanceof Badge) {
			exp = ((Badge)rewardObj).getExp();
		}

		Reward reward = Reward.of(
			principal.member(),
			request.sourceType(),
			request.sourceId(),
			request.rewardType(),
			request.rewardId(),
			request.triggerType(),
			request.triggerCount(),
			request.title(),
			request.description(),
			exp,
			request.isPublic()
		);

		return RewardDetailResponse.from(
			rewardRepository.save(reward),
			DomainResponseResolver.toResponse(source),
			RewardResponseResolver.toResponse(rewardObj)
		);
	}

	@Override
	public PagedResponse<RewardSimpleResponse> getPublicRewards(Pageable pageable) {
		return PagedResponse.of(
			rewardRepository.findByIsPublicTrue(pageable),
			r -> {
				Object source = domainObjectResolver.resolve(r.getSourceType(), r.getSourceId());
				Object rewardObj = rewardObjectResolver.resolve(r.getRewardType(), r.getRewardId());

				return RewardSimpleResponse.from(
					r,
					DomainResponseResolver.toResponse(source),
					RewardResponseResolver.toResponse(rewardObj)
				);
			}
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
			r -> {
				Object rewardObj = rewardObjectResolver.resolve(r.getRewardType(), r.getRewardId());

				return RewardSimpleResponse.from(
					r,
					DomainResponseResolver.toResponse(source),
					DomainResponseResolver.toResponse(rewardObj)
				);
			}
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
	public List<Reward> getByDomainAndTriggerType(DomainType domain, UUID domainId, TriggerType triggerType) {
		return rewardRepository.findBySourceTypeAndSourceIdAndTriggerType(domain, domainId, triggerType);
	}
}
