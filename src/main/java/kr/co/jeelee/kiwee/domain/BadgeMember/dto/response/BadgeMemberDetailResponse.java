package kr.co.jeelee.kiwee.domain.BadgeMember.dto.response;

import kr.co.jeelee.kiwee.domain.BadgeMember.entity.BadgeMember;
import kr.co.jeelee.kiwee.domain.badge.dto.response.BadgeSimpleResponse;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;

public record BadgeMemberDetailResponse(
	Long id, MemberSimpleResponse member,
	BadgeSimpleResponse badge, Integer level
) {
	public static BadgeMemberDetailResponse from(BadgeMember badgeMember) {
		return new BadgeMemberDetailResponse(
			badgeMember.getId(),
			MemberSimpleResponse.from(badgeMember.getMember()),
			BadgeSimpleResponse.from(badgeMember.getBadge(), badgeMember.getLevel()),
			badgeMember.getLevel()
		);
	}
}
