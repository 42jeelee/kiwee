package kr.co.jeelee.kiwee.domain.BadgeMember.dto.response;

import kr.co.jeelee.kiwee.domain.BadgeMember.entity.BadgeMember;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;

public record BadgeMemberSimpleResponse(
	Long id, MemberSimpleResponse member, Integer level
) {
	public static BadgeMemberSimpleResponse from(BadgeMember badgeMember) {
		return new BadgeMemberSimpleResponse(
			badgeMember.getId(),
			MemberSimpleResponse.from(badgeMember.getMember()),
			badgeMember.getLevel()
		);
	}
}
