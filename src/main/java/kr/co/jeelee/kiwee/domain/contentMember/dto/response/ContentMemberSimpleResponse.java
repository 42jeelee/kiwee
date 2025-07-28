package kr.co.jeelee.kiwee.domain.contentMember.dto.response;

import kr.co.jeelee.kiwee.domain.content.dto.response.ContentSimpleResponse;
import kr.co.jeelee.kiwee.domain.contentMember.entity.ContentMember;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;

public record ContentMemberSimpleResponse(
	ContentSimpleResponse content, MemberSimpleResponse member,
	Integer recommended, String recommendReason, Double star
) {
	public static ContentMemberSimpleResponse from(ContentMember contentMember) {
		return new ContentMemberSimpleResponse(
			ContentSimpleResponse.from(contentMember.getContent()),
			MemberSimpleResponse.from(contentMember.getMember()),
			contentMember.getRecommended(),
			contentMember.getRecommendReason(),
			contentMember.getStar()
		);
	}
}
