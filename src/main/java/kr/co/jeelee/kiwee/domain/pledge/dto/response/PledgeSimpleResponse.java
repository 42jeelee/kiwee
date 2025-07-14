package kr.co.jeelee.kiwee.domain.pledge.dto.response;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.pledge.entity.Pledge;

public record PledgeSimpleResponse(
	UUID id, String title, MemberSimpleResponse proposer
) {
	public static PledgeSimpleResponse from(Pledge pledge) {
		return new PledgeSimpleResponse(
			pledge.getId(),
			pledge.getTitle(),
			MemberSimpleResponse.from(pledge.getProposer())
		);
	}
}
