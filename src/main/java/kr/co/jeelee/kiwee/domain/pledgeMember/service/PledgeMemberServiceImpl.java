package kr.co.jeelee.kiwee.domain.pledgeMember.service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.pledge.entity.Pledge;
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
import kr.co.jeelee.kiwee.global.model.RepeatConditionField;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import kr.co.jeelee.kiwee.global.util.SecurityUtil;
import kr.co.jeelee.kiwee.global.vo.RepeatCondition;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PledgeMemberServiceImpl implements PledgeMemberService {

	private final PledgeRepository  pledgeRepository;
	private final PledgeMemberRepository pledgeMemberRepository;

	private final MemberService memberService;

	private final DomainObjectResolver domainObjectResolver;

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

		validateCondition(request.condition(), pledge.getAllowedCustomFields());
		PledgeMember pledgeMember = PledgeMember.of(
			pledge,
			member,
			request.startAt(),
			request.condition()
		);

		return PledgeMemberDetailResponse.from(pledgeMemberRepository.save(pledgeMember), domainObjectResolver);
	}

	@Override
	public PledgeMemberDetailResponse getPledge(UUID id) {
		return PledgeMemberDetailResponse.from(getById(id), domainObjectResolver);
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

	private void validateCondition(RepeatCondition condition, Set<RepeatConditionField> allowedFields) {
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

	@Override
	public PledgeMember getById(UUID id) {
		return pledgeMemberRepository.findById(id)
			.orElseThrow(PledgeMemberNotFoundException::new);
	}
}
