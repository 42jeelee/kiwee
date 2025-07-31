package kr.co.jeelee.kiwee.domain.reward.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimbusds.jose.util.Pair;

import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.domain.memberActivity.service.MemberActivityService;
import kr.co.jeelee.kiwee.domain.reward.dto.request.RewardCreateRequest;
import kr.co.jeelee.kiwee.domain.reward.dto.response.RewardDetailResponse;
import kr.co.jeelee.kiwee.domain.reward.dto.response.RewardSimpleResponse;
import kr.co.jeelee.kiwee.domain.reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.reward.exception.RewardNotFoundException;
import kr.co.jeelee.kiwee.domain.reward.model.RewardType;
import kr.co.jeelee.kiwee.domain.reward.repository.RewardRepository;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.rewardMember.service.RewardMemberService;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.model.PermissionType;
import kr.co.jeelee.kiwee.domain.badge.entity.Badge;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.channelMember.service.ChannelMemberService;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.model.RewardMatchPolicy;
import kr.co.jeelee.kiwee.global.model.RewardRepeatPolicy;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import kr.co.jeelee.kiwee.global.util.TermUtil;
import kr.co.jeelee.kiwee.global.vo.RewardCondition;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RewardServiceImpl implements RewardService {

	private final RewardRepository rewardRepository;

	private final DomainObjectResolver domainObjectResolver;

	private final ChannelMemberService channelMemberService;
	private final MemberActivityService memberActivityService;
	private final RewardMemberService rewardMemberService;

	@Override
	@Transactional
	public RewardDetailResponse createReward(CustomOAuth2User principal, RewardCreateRequest request) {
		Object source = domainObjectResolver.resolve(request.condition().sourceType(), request.condition().sourceId());

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

		if (request.condition().activityType() == ActivityType.END && request.duration() == null) {
			throw new FieldValidationException("duration", "활동 종료 보상에는 플레이 기간이 포함되어야 합니다.");
		}

		Reward reward = Reward.of(
			principal.member(),
			request.condition().toRewardCondition(),
			request.rewardType(),
			request.rewardId(),
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
			rewardRepository.findByCondition_Criterion_DomainTypeAndCondition_Criterion_DomainIdAndIsPublicTrue(
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
		return rewardRepository
			.findByCondition_Criterion_DomainTypeAndCondition_Criterion_DomainIdIsNullAndCondition_Criterion_ActivityType(
			domainType, activityType
		);
	}

	@Override
	public List<Reward> getSpecificRewards(DomainType domain, UUID domainId, ActivityType activityType) {
		return rewardRepository
			.findByCondition_Criterion_DomainTypeAndCondition_Criterion_DomainIdAndCondition_Criterion_ActivityType(
				domain, domainId, activityType
			);
	}

	@Override
	public List<Reward> getMatchRewards(MemberActivity activity) {
		List<Reward> rewards = getGeneralRewards(activity.getSourceType(), activity.getType());
		rewards.addAll(getSpecificRewards(activity.getSourceType(), activity.getSourceId(), activity.getType()));

		return rewards.stream()
			.filter(r -> matches(r, activity))
			.toList();
	}

	private boolean matches(Reward reward, MemberActivity activity) {
		RewardCondition condition = reward.getCondition();

		if (!isCoolDownOver(activity.getActor().getId(), reward.getId(), condition.repeatPolicy())) {
			return false;
		}

		if (
			condition.timeRange() != null &&
			!condition.timeRange().isWithin(activity.getCreatedAt().toLocalTime())
		) {
			return false;
		}

		if (activity.getType() == ActivityType.END && condition.duration() != null) {
			Duration playTime = memberActivityService.getPlayDurationByTerm(
				activity.getActor().getId(),
				condition.criterion().domainType(),
				condition.criterion().domainId(),
				condition.criterion().activityType(),
				LocalDate.of(2000, 1, 1).atStartOfDay(),
				LocalDateTime.now()
			);
			if (condition.duration().compareTo(playTime) > 0) {
				return false;
			}
		}

		int activityCount = getActivityCount(activity, condition);

		if (condition.criterion().activityCount() > activityCount) {
			return false;
		}

		return isConsecutive(activity.getActor().getId(), condition);
	}

	private boolean isCoolDownOver(UUID awardeeId, UUID rewardId, RewardRepeatPolicy repeatPolicy) {
		LocalDate lastGetTime =
			rewardMemberService.getDateByLastGetReward(awardeeId, rewardId);

		LocalDate startTerm = repeatPolicy.getTermType() != null
			? TermUtil.getStartTerm(repeatPolicy.getTermType(), LocalDate.now())
			: null;

		boolean isLastGet = lastGetTime != null;

		return switch (repeatPolicy) {
			case ONCE -> !isLastGet;
			case DAILY_ONCE -> !isLastGet || lastGetTime.isBefore(LocalDate.now());
			case WEEKLY_ONCE, MONTHLY_ONCE -> !isLastGet || TermUtil.getStartTerm(repeatPolicy.getTermType(), lastGetTime).isBefore(startTerm);
			case EVERY_DAY -> true;
		};
	}

	private int getActivityCount(MemberActivity activity, RewardCondition condition) {
		boolean isGeneral = activity.getSourceId() == null;

		if (
			!(condition.repeatPolicy() == RewardRepeatPolicy.ONCE ||
			condition.repeatPolicy() == RewardRepeatPolicy.EVERY_DAY)
		) {
			Pair<LocalDateTime, LocalDateTime> term = TermUtil.getTermPeriod(
				condition.repeatPolicy().getTermType(),
				0
			);

			int selfActivityCount = condition.contentType() == null
				? memberActivityService.countCriterionAtTime(
					activity.getActor().getId(),
					condition.criterion(),
					term.getLeft(),
					term.getRight()
				)
				: memberActivityService.getGeneralContentTypeCountByTerm(
					activity.getId(),
					condition.contentType(),
					term.getLeft(),
					term.getRight()
			);

			return !isGeneral && condition.matchPolicy() != RewardMatchPolicy.EXACT
				? selfActivityCount + memberActivityService.countChildrenByContentIdAndTerm(
					activity.getActor().getId(),
					activity.getSourceId(),
					activity.getType(),
					term.getLeft(),
					term.getRight()
				) : selfActivityCount;
		}

		int selfActivityCount = condition.contentType() != null
				? memberActivityService.getGeneralContentTypeCount(activity.getId(), condition.contentType())
				: memberActivityService.countByCriterion(activity.getActor().getId(), condition.criterion());

		return !isGeneral && condition.matchPolicy() != RewardMatchPolicy.EXACT
			? selfActivityCount + memberActivityService.countChildrenByContentId(
				activity.getActor().getId(),
				activity.getSourceId(),
				activity.getType()
			) : selfActivityCount;
	}

	private boolean isConsecutive(UUID actorId, RewardCondition condition) {
		if (condition.consecutiveCount() == 0) {
			return true;
		}

		int consecutiveCount = condition.contentType() != null
			? memberActivityService.countConsecutiveCount(
				actorId,
				condition.criterion(),
				condition.repeatPolicy().getTermType(),
				condition.contentType(),
				condition.consecutiveCount()
			)
			: memberActivityService.countConsecutiveCount(
				actorId,
				condition.criterion(),
				condition.repeatPolicy().getTermType(),
				condition.consecutiveCount()
			);

		return consecutiveCount >= condition.consecutiveCount();
	}
}
