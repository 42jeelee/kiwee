package kr.co.jeelee.kiwee.domain.pledge.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.pledge.dto.request.PledgeCreateRequest;
import kr.co.jeelee.kiwee.domain.pledge.dto.response.PledgeDetailResponse;
import kr.co.jeelee.kiwee.domain.pledge.dto.response.PledgeSimpleResponse;
import kr.co.jeelee.kiwee.domain.pledge.entity.Pledge;
import kr.co.jeelee.kiwee.domain.pledge.entity.PledgeRule;
import kr.co.jeelee.kiwee.domain.pledge.exception.PledgeNotFoundException;
import kr.co.jeelee.kiwee.domain.pledge.repository.PledgeRepository;
import kr.co.jeelee.kiwee.domain.pledgeMember.service.PledgeMemberService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.model.TermType;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PledgeServiceImpl implements PledgeService {

	private final PledgeRepository pledgeRepository;

	private final MemberService memberService;
	private final PledgeMemberService pledgeMemberService;

	private final DomainObjectResolver domainObjectResolver;

	@Override
	@Transactional
	public PledgeDetailResponse createPledge(UUID proposerId, PledgeCreateRequest request) {
		Member proposer = memberService.getById(proposerId);

		Pledge pledge = Pledge.of(
			request.title(),
			request.description(),
			proposer,
			request.completedLimit(),
			request.termType()
		);

		request.rules().forEach(prr -> {
			PledgeRule pledgeRule = PledgeRule.of(
				pledge,
				prr.criterion().toDomain(),
				prr.condition(),
				prr.allowedCustomFields()
			);

			pledge.addRule(pledgeRule);
		});

		Pledge savedPledge = pledgeRepository.save(pledge);

		pledgeMemberService.joinPledge(proposerId, savedPledge.getId(), request.joinRequest());

		return PledgeDetailResponse.from(savedPledge, domainObjectResolver);
	}

	@Override
	public PledgeDetailResponse getPledgeDetail(UUID id) {
		return PledgeDetailResponse.from(getById(id), domainObjectResolver);
	}

	@Override
	public PagedResponse<PledgeSimpleResponse> getPledges(UUID proposerId, TermType termType, Pageable pageable) {
		Member proposer = proposerId != null
			? memberService.getById(proposerId)
			: null;

		return PagedResponse.of(
			fetchPledges(proposer, termType, pageable),
			PledgeSimpleResponse::from
		);
	}

	@Override
	@Transactional
	public void deletePledge(UUID proposerId, UUID id) {
		Member proposer = memberService.getById(proposerId);
		Pledge pledge = getById(id);

		if (!pledge.getProposer().getId().equals(proposer.getId())) {
			throw new AccessDeniedException("해당 약속을 건 맴버만 지울 수 있습니다.");
		}

		pledgeRepository.delete(pledge);
	}

	@Override
	public Pledge getById(UUID id) {
		return pledgeRepository.findById(id)
			.orElseThrow(PledgeNotFoundException::new);
	}

	private Page<Pledge> fetchPledges(Member proposer, TermType termType, Pageable pageable) {
		boolean hasProposer = proposer != null;
		boolean hasTermType = termType != null;

		if (hasProposer && hasTermType) {
			return pledgeRepository.findByProposerAndTermType(proposer, termType, pageable);
		}

		if (hasProposer) {
			return pledgeRepository.findByProposer(proposer, pageable);
		}

		if  (hasTermType) {
			return pledgeRepository.findByTermType(termType, pageable);
		}

		return pledgeRepository.findAll(pageable);
	}

}
