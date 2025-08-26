package kr.co.jeelee.kiwee.domain.contentMember.dto.response;

public record ContentMemberCompletedRateResponse(
	double completedRate
) {
	public static ContentMemberCompletedRateResponse from(double completedRate) {
		return new ContentMemberCompletedRateResponse(completedRate);
	}
}
