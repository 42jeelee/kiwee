package kr.co.jeelee.kiwee.domain.BadgeMember.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.BadgeMember.dto.response.BadgeMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.BadgeMember.dto.response.BadgeMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.BadgeMember.entity.BadgeMember;
import kr.co.jeelee.kiwee.domain.BadgeMember.exception.BadgeMaxLevelException;
import kr.co.jeelee.kiwee.domain.BadgeMember.exception.BadgeMemberNotFoundException;
import kr.co.jeelee.kiwee.domain.BadgeMember.repository.BadgeMemberRepository;
import kr.co.jeelee.kiwee.domain.badge.dto.response.BadgeSimpleResponse;
import kr.co.jeelee.kiwee.domain.badge.entity.Badge;
import kr.co.jeelee.kiwee.domain.badge.service.BadgeService;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BadgeMemberServiceImpl implements BadgeMemberService {

	private final BadgeMemberRepository badgeMemberRepository;

	private final MemberService memberService;
	private final BadgeService badgeService;

	@Override
	public BadgeMemberDetailResponse findById(UUID memberId, UUID badgeId) {
		Member member = memberService.getById(memberId);
		Badge badge = badgeService.getById(badgeId);

		return badgeMemberRepository.findByBadgeAndMember(badge, member)
			.map(BadgeMemberDetailResponse::from)
			.orElseThrow(BadgeMemberNotFoundException::new);
	}

	@Override
	public PagedResponse<BadgeMemberSimpleResponse> getMembersByBadgeId(UUID badgeId, Pageable pageable) {
		Badge badge = badgeService.getById(badgeId);

		return PagedResponse.of(
			badgeMemberRepository.findByBadge(badge, pageable),
			BadgeMemberSimpleResponse::from
		);
	}

	@Override
	public PagedResponse<BadgeSimpleResponse> getBadgesByMemberId(UUID memberId, Pageable pageable) {
		Member member = memberService.getById(memberId);

		return PagedResponse.of(
			badgeMemberRepository.findByMember(member, pageable),
			bm -> BadgeSimpleResponse.from(bm.getBadge(), bm.getLevel())
		);
	}

	@Override
	@Transactional
	public void earnBadge(UUID memberId, UUID badgeId) {
		Member member = memberService.getById(memberId);
		Badge badge = badgeService.getById(badgeId);

		BadgeMember badgeMember = badgeMemberRepository.findByBadgeAndMember(badge, member)
			.map(bm -> {
				if (!bm.levelUp()) {
					throw new BadgeMaxLevelException();
				}
				return bm;
			})
			.orElseGet(() -> BadgeMember.of(member, badge));

		badgeMemberRepository.save(badgeMember);
	}

	@Override
	public void revokeBadge(UUID memberId, UUID badgeId) {
		Member member = memberService.getById(memberId);
		Badge badge = badgeService.getById(badgeId);

		BadgeMember badgeMember = badgeMemberRepository.findByBadgeAndMember(badge, member)
				.orElseThrow(BadgeMemberNotFoundException::new);

		badgeMemberRepository.delete(badgeMember);

	}
}
