package kr.co.jeelee.kiwee.domain.pledgeMember.dto.response;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.pledge.dto.response.PledgeSimpleResponse;
import kr.co.jeelee.kiwee.domain.pledgeMember.entity.PledgeMember;
import kr.co.jeelee.kiwee.domain.pledgeMember.model.PledgeStatusType;

public record PledgeMemberSimpleResponse(
	UUID id, PledgeSimpleResponse pledge, MemberSimpleResponse member, PledgeStatusType status
) {
	public static PledgeMemberSimpleResponse from(PledgeMember pledgeMember) {
		return new PledgeMemberSimpleResponse(
			pledgeMember.getId(),
			PledgeSimpleResponse.from(pledgeMember.getPledge()),
			MemberSimpleResponse.from(pledgeMember.getMember()),
			pledgeMember.getStatus()
		);
	}
}
