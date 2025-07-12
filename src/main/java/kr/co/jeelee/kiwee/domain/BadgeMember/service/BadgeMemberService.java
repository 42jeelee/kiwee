package kr.co.jeelee.kiwee.domain.BadgeMember.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.BadgeMember.dto.response.BadgeMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.BadgeMember.dto.response.BadgeMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.badge.dto.response.BadgeSimpleResponse;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface BadgeMemberService {

	BadgeMemberDetailResponse findById(UUID memberId, UUID badgeId);

	PagedResponse<BadgeMemberSimpleResponse> getMembersByBadgeId(UUID badgeId, Pageable pageable);
	PagedResponse<BadgeSimpleResponse> getBadgesByMemberId(UUID memberId, Pageable pageable);

	void earnBadge(UUID memberId, UUID badgeId);

	void revokeBadge(UUID memberId, UUID badgeId);

}
