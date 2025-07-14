package kr.co.jeelee.kiwee.domain.pledgeMember.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimbusds.jose.util.Pair;

import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.domain.memberActivity.event.MemberActivityEvent;
import kr.co.jeelee.kiwee.domain.memberActivity.service.MemberActivityService;
import kr.co.jeelee.kiwee.domain.pledge.entity.Pledge;
import kr.co.jeelee.kiwee.domain.pledge.entity.PledgeRule;
import kr.co.jeelee.kiwee.domain.pledge.exception.PledgeNotFoundException;
import kr.co.jeelee.kiwee.domain.pledge.repository.PledgeRepository;
import kr.co.jeelee.kiwee.domain.pledgeMember.dto.request.PledgeMemberCreateRequest;
import kr.co.jeelee.kiwee.domain.pledgeMember.dto.response.PledgeMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.pledgeMember.dto.response.PledgeMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.pledgeMember.entity.PledgeMember;
import kr.co.jeelee.kiwee.domain.pledgeMember.exception.PledgeMemberInvalidException;
import kr.co.jeelee.kiwee.domain.pledgeMember.exception.PledgeMemberNotFoundException;
import kr.co.jeelee.kiwee.domain.pledgeMember.model.PledgeStatusType;
import kr.co.jeelee.kiwee.domain.pledgeMember.repository.PledgeMemberRepository;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.model.RepeatConditionField;
import kr.co.jeelee.kiwee.global.model.TermType;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import kr.co.jeelee.kiwee.global.util.SecurityUtil;
import kr.co.jeelee.kiwee.global.util.TermUtil;
import kr.co.jeelee.kiwee.global.vo.ActivityCriterion;
import kr.co.jeelee.kiwee.global.vo.RepeatCondition;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PledgeMemberServiceImpl implements PledgeMemberService {

	private final PledgeRepository  pledgeRepository;
	private final PledgeMemberRepository pledgeMemberRepository;

	private final MemberService memberService;
	private final MemberActivityService memberActivityService;

	private final DomainObjectResolver domainObjectResolver;

	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public PledgeMemberDetailResponse joinPledge(UUID memberId, UUID pledgeId, PledgeMemberCreateRequest request) {
		Member member = memberService.getById(memberId);
		Pledge pledge = pledgeRepository.findById(pledgeId)
			.orElseThrow(PledgeNotFoundException::new);

		if (pledgeMemberRepository.existsByMemberIdAndPledgeIdAndStatus(
				memberId,
				pledgeId,
				PledgeStatusType.PLANNED
			)
			|| pledgeMemberRepository.existsByMemberIdAndPledgeIdAndStatus(
				memberId,
				pledgeId,
				PledgeStatusType.IN_PROGRESS
			)
		) {
			throw new PledgeMemberInvalidException("이미 동일한 약속을 진행 중이거나 예정입니다.");
		}

		if (pledge.getTermType() == TermType.NONE && request.completedAt() != null) {
			throw new PledgeMemberInvalidException("반복 약속이 아닌 경우 완료 목표 시간을 설정할 수 없습니다.");
		}

		LocalDate minStartDate = TermUtil.getStartTerm(pledge.getTermType(), 1);

		if (request.startAt().toLocalDate().isBefore(minStartDate)) {
			throw new PledgeMemberInvalidException(String.format("시작 시간은 '%s' 보다 이후여야 합니다.", minStartDate));
		}

		validateCondition(request.conditions(), pledge.getRules());
		PledgeMember pledgeMember = PledgeMember.of(
			pledge,
			member,
			request.startAt(),
			request.completedAt(),
			request.conditions() != null
				? request.conditions().stream()
				.collect(Collectors.toMap(
					cr -> cr.criterion().toDomain(),
					PledgeMemberCreateRequest.ConditionRequest::condition,
					(oldVal, newVal) -> newVal
				))
				: null
		);
		PledgeMember savedPledgeMember = pledgeMemberRepository.save(pledgeMember);

		eventPublisher.publishEvent(MemberActivityEvent.of(
			member.getId(),
			DomainType.PLEDGE,
			pledge.getId(),
			ActivityType.JOIN
		));

		return PledgeMemberDetailResponse.from(savedPledgeMember, domainObjectResolver);
	}

	@Override
	public PledgeMemberDetailResponse getPledgeMember(UUID id) {
		PledgeMember pledgeMember = getById(id);

		calculateProgress(pledgeMember);
		return PledgeMemberDetailResponse.from(pledgeMember, domainObjectResolver);
	}

	@Override
	public PagedResponse<PledgeMemberSimpleResponse> getMemberPledges(UUID memberId, Pageable pageable) {
		Member member = memberService.getById(memberId);

		return PagedResponse.of(
			pledgeMemberRepository.findByMember(member, pageable),
			PledgeMemberSimpleResponse::from
		);
	}

	@Override
	public PagedResponse<PledgeMemberSimpleResponse> getPledgeMembers(UUID pledgeId, Pageable pageable) {
		Pledge pledge = pledgeRepository.findById(pledgeId)
			.orElseThrow(PledgeNotFoundException::new);

		return PagedResponse.of(
			pledgeMemberRepository.findByPledge(pledge, pageable),
			PledgeMemberSimpleResponse::from
		);
	}

	@Override
	@Transactional
	public PledgeMemberSimpleResponse delayPledge(UUID id, LocalDateTime startAt) {
		PledgeMember pledgeMember = getById(id);
		Member member = SecurityUtil.getLoginMember();

		if (!pledgeMember.getMember().getId().equals(member.getId())) {
			throw new AccessDeniedException("해당 약속을 한 맴버가 아닙니다.");
		}

		if (startAt.isBefore(LocalDateTime.now())) {
			throw new PledgeMemberInvalidException("현재보다 앞으로 시작할 수 없습니다.");
		}

		pledgeMember.delay(startAt);

		return PledgeMemberSimpleResponse.from(pledgeMember);
	}

	@Override
	@Transactional
	public PledgeMemberSimpleResponse giveUpPledge(UUID id) {
		PledgeMember pledgeMember = getById(id);
		Member member = SecurityUtil.getLoginMember();

		if (!pledgeMember.getMember().getId().equals(member.getId())) {
			throw new AccessDeniedException("해당 약속을 한 맴버가 아닙니다.");
		}

		if (pledgeMember.getCompletedAt() != null) {
			throw new PledgeMemberInvalidException("이미 끝난 약속입니다.");
		}

		pledgeMember.fail();

		return PledgeMemberSimpleResponse.from(pledgeMember);
	}

	@Override
	public void calculateProgress(PledgeMember pledgeMember) {
		calculateProgress(pledgeMember, 0);
	}

	@Override
	public void calculateProgress(PledgeMember pledgeMember, int prev) {
		Map<ActivityCriterion, Integer> progress = new HashMap<>();
		Pair<LocalDateTime, LocalDateTime> period =
			TermUtil.getTermPeriod(pledgeMember.getPledge().getTermType(), prev);

		List<MemberActivity> termActivities =
			memberActivityService.getTimeMemberActivities(
				pledgeMember.getMember().getId(),
				period.getLeft(),
				period.getRight()
			);

		for (PledgeRule rule : pledgeMember.getPledge().getRules()) {
			int count = countCorrectProgress(pledgeMember, rule, termActivities, prev);
			progress.put(rule.getCriterion(), count);
		}

		pledgeMember.loadProgress(progress);
	}

	private int countCorrectProgress(
		PledgeMember pledgeMember,
		PledgeRule rule,
		List<MemberActivity> activities,
		int prev
	) {
		if (rule.getCondition() == null) {
			return activities.size();
		}

		RepeatCondition condition = pledgeMember.getConditions() != null
			? rule.getCondition().mergeWith(pledgeMember.getConditions().get(rule.getCriterion()))
			: rule.getCondition();

		return (int) activities.stream()
			.filter(a -> {
				LocalDateTime createdAt = a.getCreatedAt();
				return (
					condition.days() == null || TermUtil.getDateByDayOfWeek(condition.days(), prev)
						.contains(createdAt.toLocalDate())
				)
				&& (
					condition.timeRange() == null
						|| condition.timeRange().isWithin(createdAt.toLocalTime())
				)
				&& (
					condition.monthDays() == null
						|| condition.monthDays().contains(createdAt.toLocalDate().getDayOfMonth())
				)
				&& (
					condition.yearlyDates() == null
					|| condition.yearlyDates().contains(MonthDay.from(createdAt))
				);
			}).count();
	}

	private void validateCondition(
		List<PledgeMemberCreateRequest.ConditionRequest> conditions,
		List<PledgeRule> rules
	) {
		if (conditions == null || conditions.isEmpty()) {
			return;
		}
		for (PledgeMemberCreateRequest.ConditionRequest conditionRequest : conditions) {
			ActivityCriterion criterion = conditionRequest.criterion().toDomain();
			RepeatCondition condition = conditionRequest.condition();

			PledgeRule rule = rules.stream()
				.filter(r -> r.getCriterion().equals(criterion))
				.findFirst()
				.orElseThrow(() -> new PledgeMemberInvalidException("약속에 없는 조건이 포함되어 있습니다."));

			Set<RepeatConditionField> allowedFields = rule.getAllowedCustomFields();

			if (!allowedFields.contains(RepeatConditionField.TIME_RANGE) && condition.timeRange() != null) {
				throw new PledgeMemberInvalidException("해당 약속은 시간을 바꿀 수 없습니다.");
			}
			if (!allowedFields.contains(RepeatConditionField.DAYS) && condition.days() != null) {
				throw new PledgeMemberInvalidException("해당 약속은 요일을 지정할 수 없습니다.");
			}
			if (!allowedFields.contains(RepeatConditionField.MONTH_DAYS) && condition.monthDays() != null) {
				throw new PledgeMemberInvalidException("해당 약속은 날짜를 지정할 수 없습니다.");
			}
			if (!allowedFields.contains(RepeatConditionField.YEARLY_DATES) && condition.yearlyDates() != null) {
				throw new PledgeMemberInvalidException("해당 약속은 날짜를 지정할 수 없습니다.");
			}
		}
	}

	@Override
	public PledgeMember getById(UUID id) {
		return pledgeMemberRepository.findById(id)
			.orElseThrow(PledgeMemberNotFoundException::new);
	}
}
