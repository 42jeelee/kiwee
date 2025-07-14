package kr.co.jeelee.kiwee.domain.pledgeMember.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.pledge.dto.response.PledgeSimpleResponse;
import kr.co.jeelee.kiwee.domain.pledgeMember.entity.PledgeMember;
import kr.co.jeelee.kiwee.domain.pledgeMember.model.PledgeStatusType;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;

public record PledgeMemberDetailResponse(
	UUID id, PledgeSimpleResponse pledge, MemberSimpleResponse member,
	PledgeStatusType status, LocalDateTime startAt, LocalDateTime limitAt, LocalDateTime completedAt,
	List<PledgeProgressResponse> progress
) {
	public static PledgeMemberDetailResponse from(PledgeMember pledgeMember, DomainObjectResolver resolver) {
		return new PledgeMemberDetailResponse(
			pledgeMember.getId(),
			PledgeSimpleResponse.from(pledgeMember.getPledge()),
			MemberSimpleResponse.from(pledgeMember.getMember()),
			pledgeMember.getStatus(),
			pledgeMember.getStartAt(),
			pledgeMember.getLimitedAt(),
			pledgeMember.getCompletedAt(),
			pledgeMember.getProgress().entrySet().stream()
				.map(e ->
					PledgeProgressResponse.from(
						e.getKey(),
						pledgeMember.getCondition(e.getKey()),
						e.getValue(),
						resolver
					)
				).toList()
		);
	}
}
