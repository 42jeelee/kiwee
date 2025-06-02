package kr.co.jeelee.kiwee.domain.reputations.dto.response;

import java.time.YearMonth;

import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.member.entity.Member;

public record ReputationsStatResponse(
	MemberSimpleResponse member, YearMonth yearMonth,
	int netScore, int upVotes, int downVotes, int rank
) {
	public static ReputationsStatResponse of(
		MemberSimpleResponse memberResponse,
		YearMonth yearMonth,
		int netScore,
		int upVotes,
		int downVotes,
		int rank
	) {
		return  new ReputationsStatResponse(
			memberResponse,
			yearMonth,
			netScore,
			upVotes,
			downVotes,
			rank
		);
	}
}
