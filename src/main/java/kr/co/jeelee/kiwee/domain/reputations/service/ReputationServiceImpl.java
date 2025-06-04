package kr.co.jeelee.kiwee.domain.reputations.service;

import java.time.YearMonth;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.reputations.repository.ReputationsRepository;
import kr.co.jeelee.kiwee.domain.reputations.dto.request.ReputationsCreateRequest;
import kr.co.jeelee.kiwee.domain.reputations.dto.response.ReputationsStatResponse;
import kr.co.jeelee.kiwee.domain.reputations.dto.response.ReputationsVoteResponse;
import kr.co.jeelee.kiwee.domain.reputations.entity.Reputation;
import kr.co.jeelee.kiwee.domain.reputations.exception.DuplicateVoteException;
import kr.co.jeelee.kiwee.domain.reputations.exception.SelfVoteNotAllowedException;
import kr.co.jeelee.kiwee.domain.reputations.projection.ReputationStatProjection;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReputationServiceImpl implements ReputationService {

	private final ReputationsRepository reputationsRepository;

	private final MemberService memberService;

	@Override
	@Transactional
	public void vote(ReputationsCreateRequest reputationsCreateRequest) {
		Member giver = memberService.getById(reputationsCreateRequest.giverId());
		Member receiver = memberService.getById(reputationsCreateRequest.receiverId());

		if (giver.equals(receiver)) {
			throw new SelfVoteNotAllowedException();
		}

		if (reputationsRepository.existsByGiverAndYearMonth(giver, YearMonth.now())) {
			throw new DuplicateVoteException();
		}

		Reputation reputation = Reputation.of(giver, receiver,
			reputationsCreateRequest.isUp() == null || reputationsCreateRequest.isUp());

		reputationsRepository.save(reputation);
	}

	@Override
	public PagedResponse<ReputationsStatResponse> getLank(YearMonth yearMonth, Pageable pageable) {
		return PagedResponse.of(reputationsRepository.getRankingsByMonth(yearMonth.toString(), pageable), (ReputationStatProjection p) -> {
			MemberSimpleResponse memberResponse = new MemberSimpleResponse(
				p.getId(),
				p.getNickname(),
				p.getAvatarUrl(),
				p.getLevel(),
				p.getExp()
			);

			return ReputationsStatResponse.of(
				memberResponse,
				yearMonth,
				p.getNetScore(),
				p.getUpVotes(),
				p.getDownVotes(),
				p.getRank()
			);
		});
	}

	@Override
	public ReputationsStatResponse getStatByMemberId(UUID id, YearMonth yearMonth) {
		return reputationsRepository.getStatByMemberIdAndYearMonth(id, yearMonth.toString()).stream()
			.map(p -> {
				MemberSimpleResponse memberResponse = new MemberSimpleResponse(
					p.getId(),
					p.getNickname(),
					p.getAvatarUrl(),
					p.getLevel(),
					p.getExp()
				);

				return ReputationsStatResponse.of(
					memberResponse,
					yearMonth,
					p.getUpVotes() - p.getDownVotes(),
					p.getUpVotes(),
					p.getDownVotes(),
					p.getRank()
				);
			}).toList().get(0);
	}

	@Override
	public PagedResponse<ReputationsVoteResponse> getVotesByMemberId(UUID id, Pageable pageable) {
		Member member = memberService.getById(id);

		return PagedResponse.of(
			reputationsRepository.getByGiver(member, pageable),
			ReputationsVoteResponse::from
		);
	}
}
