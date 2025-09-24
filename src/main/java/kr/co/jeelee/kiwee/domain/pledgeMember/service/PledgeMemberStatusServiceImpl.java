package kr.co.jeelee.kiwee.domain.pledgeMember.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.domain.memberActivity.event.MemberActivityEvent;
import kr.co.jeelee.kiwee.domain.memberActivity.service.MemberActivityService;
import kr.co.jeelee.kiwee.domain.notification.event.NotificationEvent;
import kr.co.jeelee.kiwee.domain.notification.model.NotificationType;
import kr.co.jeelee.kiwee.domain.pledge.entity.PledgeRule;
import kr.co.jeelee.kiwee.domain.pledgeMember.entity.PledgeMember;
import kr.co.jeelee.kiwee.domain.pledgeMember.model.PledgeStatusType;
import kr.co.jeelee.kiwee.domain.pledgeMember.repository.PledgeMemberRepository;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.model.TermType;
import kr.co.jeelee.kiwee.global.util.TermUtil;
import kr.co.jeelee.kiwee.global.vo.ActivityCriterion;
import kr.co.jeelee.kiwee.global.vo.RepeatCondition;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PledgeMemberStatusServiceImpl implements PledgeMemberStatusService {

	private final PledgeMemberRepository pledgeMemberRepository;

	private final PledgeMemberService pledgeMemberService;
	private final MemberActivityService memberActivityService;

	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public void progressActivity(UUID activityId) {
		MemberActivity memberActivity = memberActivityService.getById(activityId);
		Member member = memberActivity.getActor();

		List<PledgeMember> inProgressPledge = pledgeMemberRepository.findByMemberAndStatus(
			member,
			PledgeStatusType.IN_PROGRESS
		);

		ActivityCriterion criterion = ActivityCriterion.of(
			memberActivity.getSourceType(),
			memberActivity.getSourceId(),
			memberActivity.getType()
		);

		inProgressPledge.stream()
			.filter(pm -> pm.getPledge().isIncludeCriterion(criterion))
			.forEach(this::evaluatePledgeMember);
	}

	@Override
	@Transactional
	@Scheduled(cron = "0 0/5 * * * * ")
	public void autoEvaluateTime() {
		List<PledgeMember> inProgressPledge = pledgeMemberRepository.findByStatus(PledgeStatusType.IN_PROGRESS);

		inProgressPledge.forEach(pm -> {
			checkExpired(pm);
			checkCompleted(pm);

			for (PledgeRule rule : pm.getPledge().getRules()) {
				if (pm.getStatus() != PledgeStatusType.IN_PROGRESS) break;

				if (rule.getCondition() == null) break;

				RepeatCondition mergedCondition = pm.getConditions() != null
					? rule.getCondition().mergeWith(pm.getConditions().get(rule.getCriterion()))
					: rule.getCondition();

				if (isTimeCheckTarget(pm.getPledge().getTermType(), mergedCondition)) {
					checkTimeExceeded(pm, mergedCondition);
				}
			}
		});

		List<PledgeMember> plannedPledge = pledgeMemberRepository.findByStatus(PledgeStatusType.PLANNED);

		plannedPledge.forEach(pm -> {
			if (pm.getStartAt().isBefore(LocalDateTime.now())) {
				pm.inProgress();

				eventPublisher.publishEvent(MemberActivityEvent.of(
					pm.getMember().getId(),
					DomainType.PLEDGE,
					pm.getPledge().getId(),
					ActivityType.START
				));

				eventPublisher.publishEvent(NotificationEvent.of(
					pm.getMember().getId(),
					NotificationType.PLEDGE,
					pm.getPledge().getId(),
					"약속 이행가능",
					String.format("'%s' 약속을 이행하실 수 있습니다.", pm.getPledge().getTitle()),
					null
				));
			}
		});

	}

	@Override
	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void autoEvaluateDaily() {
		autoEvaluateTerm(TermType.DAILY);
		autoEvaluateDate();
	}

	@Override
	@Transactional
	@Scheduled(cron = "0 0 0 * * MON")
	public void autoEvaluateWeekly() {
		autoEvaluateTerm(TermType.WEEKLY);
	}

	@Override
	@Transactional
	@Scheduled(cron = "0 0 0 1 * *")
	public void autoEvaluateMonthly() {
		autoEvaluateTerm(TermType.MONTHLY);
	}

	@Override
	@Transactional
	@Scheduled(cron = "0 0 0 1 1 *")
	public void autoEvaluateYearly() {
		autoEvaluateTerm(TermType.YEARLY);
	}

	private void autoEvaluateTerm(TermType termType) {
		List<PledgeMember> inProgressPledge =
			pledgeMemberRepository.findByPledge_TermTypeAndStatus(termType, PledgeStatusType.IN_PROGRESS);

		inProgressPledge.forEach(pm -> {
			checkExpired(pm);
			checkCompleted(pm);
			evaluatePledgeMember(pm, 1);
		});
	}

	private void autoEvaluateDate() {
		List<PledgeMember> inProgressPledge = pledgeMemberRepository.findByPledge_TermTypeInAndStatus(
				List.of(TermType.WEEKLY, TermType.MONTHLY, TermType.YEARLY),
				PledgeStatusType.IN_PROGRESS
			);

		inProgressPledge.forEach(pm -> {
			checkExpired(pm);
			checkCompleted(pm);

			for (PledgeRule rule : pm.getPledge().getRules()) {
				if (pm.getStatus() != PledgeStatusType.IN_PROGRESS) break;

				if (rule.getCondition() == null) break;

				RepeatCondition mergedCondition = pm.getConditions() != null
					? rule.getCondition().mergeWith(pm.getConditions().get(rule.getCriterion()))
					: rule.getCondition();

				if (isDaysCheckTarget(mergedCondition)) {
					checkDaysExceeded(pm, rule.getCriterion(), mergedCondition);
				}
			}
		});

	}

	private void checkCompleted(PledgeMember pledgeMember) {
		if (
			pledgeMember.getStatus() == PledgeStatusType.IN_PROGRESS
			&& pledgeMember.getCompletedAt() != null
			&& pledgeMember.getCompletedAt().isBefore(LocalDateTime.now())
		) {
			successPledgeMember(pledgeMember);
		}
	}

	private void checkExpired(PledgeMember pledgeMember) {
		if (
			pledgeMember.getStatus() == PledgeStatusType.IN_PROGRESS
			&& pledgeMember.getLimitedAt() != null
			&& pledgeMember.getLimitedAt().isBefore(LocalDateTime.now())
		) {
			evaluatePledgeMember(pledgeMember);
		}
	}

	private void checkDaysExceeded(
		PledgeMember pledgeMember,
		ActivityCriterion criterion,
		RepeatCondition condition
	) {
		if (pledgeMember.getStatus() != PledgeStatusType.IN_PROGRESS) {
			return;
		}

		LocalDate yesterday = LocalDate.now().minusDays(1);

		if (!isDaysCheckTarget(condition)) {
			return;
		}

		int count = memberActivityService.countCriterionAtTime(
			pledgeMember.getMember().getId(),
			criterion,
			yesterday.atStartOfDay(),
			yesterday.plusDays(1).atStartOfDay()
		);

		if (count < criterion.getActivityCount()) {
			evaluatePledgeMember(pledgeMember);
		}

	}

	private void checkTimeExceeded(PledgeMember pledgeMember, RepeatCondition condition) {
		if (condition.timeRange() == null || pledgeMember.getStatus() != PledgeStatusType.IN_PROGRESS) {
			return;
		}

		if (condition.timeRange().getEndTime().isBefore(LocalTime.now())) {
			evaluatePledgeMember(pledgeMember);
		}
	}

	private boolean isDaysCheckTarget(RepeatCondition condition) {
		if (condition == null) return false;

		if (isIncludeTime(condition)) {
			return false;
		}

		if (!isIncludeDays(condition)) {
			return false;
		}

		return TermUtil.isTodayMatched(condition, LocalDate.now().minusDays(1));
	}

	private boolean isTimeCheckTarget(TermType termType, RepeatCondition condition) {
		if (condition == null) return false;

		if (termType == TermType.NONE) {
			return false;
		}

		if (!isIncludeTime(condition)) {
			return false;
		}

		if (termType == TermType.DAILY) {
			return true;
		}

		return TermUtil.isTodayMatched(condition, LocalDate.now());
	}

	private boolean isIncludeTime(RepeatCondition condition) {
		if (condition == null) return false;
		return condition.timeRange() != null;
	}

	private boolean isIncludeDays(RepeatCondition condition) {
		if (condition == null) return false;
		return condition.days() != null;
	}

	private void evaluatePledgeMember(PledgeMember pledgeMember, int prevTerm) {
		pledgeMemberService.calculateProgress(pledgeMember, prevTerm);

		if (pledgeMember.isClearAllCriteria()) {
			if (pledgeMember.getPledge().getTermType() == TermType.NONE) {
				successPledgeMember(pledgeMember);
				return;
			}
			passPledgeMember(pledgeMember);
			return;
		}
		failPledgeMember(pledgeMember);
	}

	private void evaluatePledgeMember(PledgeMember pledgeMember) {
		evaluatePledgeMember(pledgeMember, 0);
	}

	private void passPledgeMember(PledgeMember pledgeMember) {
		eventPublisher.publishEvent(MemberActivityEvent.of(
			pledgeMember.getMember().getId(),
			DomainType.PLEDGE,
			pledgeMember.getPledge().getId(),
			ActivityType.PASS
		));
	}

	private void successPledgeMember(PledgeMember pledgeMember) {
		pledgeMember.success();

		eventPublisher.publishEvent(MemberActivityEvent.of(
			pledgeMember.getMember().getId(),
			DomainType.PLEDGE,
			pledgeMember.getPledge().getId(),
			ActivityType.COMPLETE
		));

		eventPublisher.publishEvent(NotificationEvent.of(
			pledgeMember.getMember().getId(),
			NotificationType.PLEDGE,
			pledgeMember.getPledge().getId(),
			pledgeMember.getPledge().getTitle(),
			String.format("'%s'을(를) 성공했습니다:)", pledgeMember.getPledge().getTitle()),
			null
		));
	}

	private void failPledgeMember(PledgeMember pledgeMember) {
		pledgeMember.fail();

		eventPublisher.publishEvent(MemberActivityEvent.of(
			pledgeMember.getMember().getId(),
			DomainType.PLEDGE,
			pledgeMember.getPledge().getId(),
			ActivityType.FAILED
		));

		eventPublisher.publishEvent(NotificationEvent.of(
			pledgeMember.getMember().getId(),
			NotificationType.PLEDGE,
			pledgeMember.getPledge().getId(),
			pledgeMember.getPledge().getTitle(),
			String.format("'%s'을(를) 실패하였습니다..", pledgeMember.getPledge().getTitle()),
			null
		));
	}

}
