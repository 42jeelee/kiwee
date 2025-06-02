package kr.co.jeelee.kiwee.domain.reputations.dto.response;

import java.time.LocalDateTime;
import java.time.YearMonth;

import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.reputations.entity.Reputation;

public record ReputationsVoteResponse(
	MemberSimpleResponse member, YearMonth yearMonth,
	boolean isUp, LocalDateTime voteAt
) {
	public static ReputationsVoteResponse from(Reputation reputation) {
		return new ReputationsVoteResponse(
			MemberSimpleResponse.from(reputation.getGiver()),
			reputation.getYearMonth(),
			reputation.isUp(),
			reputation.getCreatedAt()
		);
	}
}
