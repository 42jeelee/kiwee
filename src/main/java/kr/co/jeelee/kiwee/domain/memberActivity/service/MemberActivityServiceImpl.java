package kr.co.jeelee.kiwee.domain.memberActivity.service;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.memberActivity.dto.response.MemberActivityResponse;
import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.domain.memberActivity.exception.MemberActivityNotFoundException;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.domain.memberActivity.repository.MemberActivityRepository;
import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.model.TermType;
import kr.co.jeelee.kiwee.global.util.TermUtil;
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
	public int countChildrenByContentId(UUID actorId, UUID contentId, ActivityType activityType) {
		return memberActivityRepository.countActivitiesInChildContents(actorId, contentId, activityType);
	}

	@Override
	public int countChildrenByContentIdAndTerm(UUID actorId, UUID contentId, ActivityType activityType,
		LocalDateTime start, LocalDateTime end) {
		return memberActivityRepository.countActivitiesInChildContents(actorId, contentId, activityType, start, end);
	}

	@Override
	public int countCriterionAtTime(UUID actorId, ActivityCriterion criterion, LocalDateTime start, LocalDateTime end) {
		return criterion.domainId() != null
			? memberActivityRepository.countByActorIdAndSourceTypeAndSourceIdAndTypeAndCreatedAtBetween(
				actorId, criterion.domainType(), criterion.domainId(), criterion.activityType(), start, end
			)
			: memberActivityRepository.countByActorIdAndSourceTypeAndTypeAndCreatedAtBetween(
				actorId, criterion.domainType(), criterion.activityType(), start, end
		);
	}

	@Override
	public int countConsecutiveCount(UUID actorId, ActivityCriterion criterion, TermType termType, int num) {
		List<java.sql.Date> activitiesDates = criterion.domainId() == null
			? memberActivityRepository.findActivityDatesByCriterion(
				actorId,
				criterion.domainType().name(),
				criterion.activityType().name(),
				num
			)
			:  memberActivityRepository.findActivityDatesByCriterion(
				actorId,
				criterion.domainType().name(),
				criterion.domainId(),
				criterion.activityType().name(),
				num
			);

		return countConsecutiveByDates(
			activitiesDates.stream().map(Date::toLocalDate).collect(Collectors.toList()),
			termType, num
		);
	}

	@Override
	public int countConsecutiveCount(UUID actorId, ActivityCriterion criterion, TermType termType, ContentType contentType, int num) {
		if (criterion.domainId() != null) {
			return countConsecutiveCount(actorId, criterion, termType, num);
		}

		List<LocalDate> activitiesDates = memberActivityRepository.findActivityDatesByCriterion(
			actorId,
			contentType,
			criterion.activityType(),
			num
		);

		return countConsecutiveByDates(activitiesDates, termType, num);
	}

	@Override
	public int countByCriterion(UUID actorId, ActivityCriterion criterion) {
		return criterion.domainId() == null
			? memberActivityRepository.countByActorIdAndSourceTypeAndType(
				actorId,
				criterion.domainType(),
				criterion.activityType()
			)
			: memberActivityRepository.countByActorIdAndSourceTypeAndSourceIdAndType(
				actorId,
				criterion.domainType(),
				criterion.domainId(),
				criterion.activityType()
			);
	}

	@Override
	public int getGeneralContentTypeCount(UUID id, ContentType contentType) {
		MemberActivity activity = getById(id);

		return memberActivityRepository.countActivitiesByContentType(
			activity.getActor().getId(),
			contentType,
			activity.getType()
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
	public int getGeneralContentTypeCountByTerm(UUID id, ContentType contentType, LocalDateTime start,
		LocalDateTime end) {
		MemberActivity activity = getById(id);

		return memberActivityRepository.countActivitiesByContentTypeAndTerm(
			activity.getActor().getId(),
			contentType,
			activity.getType(),
			start,
			end
		);
	}

	@Override
	public MemberActivity getById(UUID id) {
		return memberActivityRepository.findById(id)
			.orElseThrow(MemberActivityNotFoundException::new);
	}

	private int countConsecutiveByDates(List<LocalDate> dates, TermType termType, int num) {
		if (dates == null || dates.isEmpty()) {
			return 0;
		}
		LocalDate currStartDate = TermUtil.getStartTerm(termType, LocalDate.now());

		List<LocalDate> dateList = dates.stream()
			.filter(date -> TermUtil.getStartTerm(termType, date).equals(currStartDate))
			.toList();

		if (dateList.size() < 2) {
			return dateList.size();
		}

		int streak = 1, max = 0;
		long prev = dateList.get(0).toEpochDay();
		for (int i = 1; i < dateList.size(); i++) {
			long day = dateList.get(i).toEpochDay();

			if (day == prev - 1) {
				streak++;
			} else {
				streak = 1;
			}

			if (streak > max) {
				max = streak;
			}
			prev = day;
		}

		return max;
	}
}
