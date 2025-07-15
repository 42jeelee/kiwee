package kr.co.jeelee.kiwee.domain.memberActivity.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.memberActivity.dto.response.MemberActivityResponse;
import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.domain.memberActivity.exception.MemberActivityNotFoundException;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.domain.memberActivity.repository.MemberActivityRepository;
import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.vo.ActivityCriterion;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberActivityServiceImpl implements MemberActivityService {

	private final MemberActivityRepository memberActivityRepository;

	@Override
	public PagedResponse<MemberActivityResponse> memberActivities(UUID memberId, Pageable pageable) {
		return PagedResponse.of(
			memberActivityRepository.findByActorId(memberId, pageable),
			MemberActivityResponse::from
		);
	}

	@Override
	public UUID log(Member actor, ActivityType type, DomainType sourceType, UUID sourceId, String description) {
		return log(actor, type, sourceType, sourceId, description, new ArrayList<>());
	}

	@Override
	public UUID log(Member actor, ActivityType type, DomainType sourceType, UUID sourceId, String description, List<RewardMember> rewardMembers) {
		MemberActivity memberActivity = MemberActivity.of(actor, type, sourceType, sourceId, description, rewardMembers);
		return memberActivityRepository.save(memberActivity).getId();
	}

	@Override
	public void addRewards(UUID activityId, List<RewardMember> rewardMembers) {
		MemberActivity memberActivity = getById(activityId);

		memberActivity.addRewardMembers(rewardMembers);
		memberActivityRepository.save(memberActivity);
	}

	@Override
	public List<MemberActivity> getTimeMemberActivities(UUID actorId, LocalDateTime start, LocalDateTime end) {
		return memberActivityRepository.findByActorIdAndCreatedAtBetween(
			actorId,
			start,
			end
		);
	}

	@Override
	public List<MemberActivity> getTimeAllActivities(LocalDateTime start, LocalDateTime end) {
		return memberActivityRepository.findByCreatedAtBetween(
			start,
			end
		);
	}

	@Override
	public Duration getPlayDuration(MemberActivity endActivity) {
		if (endActivity.getType() != ActivityType.END) {
			return Duration.ZERO;
		}

		MemberActivity playActivity =
			memberActivityRepository.findFirstByActorIdAndSourceTypeAndSourceIdAndTypeInOrderByCreatedAtDesc(
				endActivity.getActor().getId(),
				endActivity.getSourceType(),
				endActivity.getSourceId(),
				List.of(ActivityType.PLAY, ActivityType.END)
			).orElse(null);

		if (playActivity == null || playActivity.getType() != ActivityType.PLAY) {
			return Duration.ZERO;
		}

		return Duration.between(playActivity.getCreatedAt(), endActivity.getCreatedAt());
	}

	@Override
	public Duration getPlayDurationByTerm(UUID actorId, DomainType sourceType, UUID sourceId, ActivityType activityType,
		LocalDateTime start, LocalDateTime end) {
		List<MemberActivity> playActivities =
			memberActivityRepository.findByActorIdAndSourceTypeAndSourceIdAndTypeInAndCreatedAtBetweenOrderByCreatedAtAsc(
				actorId,
				sourceType,
				sourceId,
				List.of(ActivityType.PLAY, ActivityType.END),
				start,
				end
			);

		Duration duration = playActivities.get(0).getType() == ActivityType.END
			? Duration.between(start, playActivities.get(0).getCreatedAt())
			: Duration.ZERO;
		MemberActivity prevActivity = null;
		for (MemberActivity activity : playActivities) {
			if (
				prevActivity != null &&
				(prevActivity.getType() == ActivityType.PLAY && activity.getType() == ActivityType.END)
			) {
				duration = duration.plus(Duration.between(prevActivity.getCreatedAt(), activity.getCreatedAt()));
			}
			prevActivity = activity;
		}

		if (prevActivity != null && prevActivity.getType() == ActivityType.PLAY) {
			duration = duration.plus(Duration.between(prevActivity.getCreatedAt(), end));
		}

		return duration;
	}

	@Override
	public boolean existsActivityByCriterionAtTime(
		UUID actorId, ActivityCriterion criterion, LocalDateTime start, LocalDateTime end
	) {
		return criterion.domainId() != null
			? memberActivityRepository.existsByActorIdAndSourceTypeAndSourceIdAndTypeAndCreatedAtBetween(
				actorId, criterion.domainType(), criterion.domainId(), criterion.activityType(), start, end
			)
			: memberActivityRepository.existsByActorIdAndSourceTypeAndSourceIdIsNullAndTypeAndCreatedAtBetween(
				actorId, criterion.domainType(), criterion.activityType(), start, end
		);
	}

	@Override
	public int countCriterionAtTime(UUID actorId, ActivityCriterion criterion, LocalDateTime start, LocalDateTime end) {
		return criterion.domainId() != null
			? memberActivityRepository.countByActorIdAndSourceTypeAndSourceIdAndTypeAndCreatedAtBetween(
				actorId, criterion.domainType(), criterion.domainId(), criterion.activityType(), start, end
			)
			: memberActivityRepository.countByActorIdAndSourceTypeAndSourceIdIsNullAndTypeAndCreatedAtBetween(
				actorId, criterion.domainType(), criterion.activityType(), start, end
		);
	}

	@Override
	public int getGeneralCount(UUID id) {
		MemberActivity activity = getById(id);

		return memberActivityRepository.countByActorIdAndSourceTypeAndType(
			activity.getActor().getId(),
			activity.getSourceType(),
			activity.getType()
		);
	}

	@Override
	public int getSpecificCount(UUID id) {
		MemberActivity activity = getById(id);

		return memberActivityRepository.countByActorIdAndSourceTypeAndSourceIdAndType(
			activity.getActor().getId(),
			activity.getSourceType(),
			activity.getSourceId(),
			activity.getType()
		);
	}

	@Override
	public MemberActivity getById(UUID id) {
		return memberActivityRepository.findById(id)
			.orElseThrow(MemberActivityNotFoundException::new);
	}
}
