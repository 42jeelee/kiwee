package kr.co.jeelee.kiwee.domain.contentMember.dto.response;

public record ContentMemberStarResponse(
	Double star, Long count
) {
	public static ContentMemberStarResponse of(Double star, Long count) {
		return new ContentMemberStarResponse(star, count);
	}
}
