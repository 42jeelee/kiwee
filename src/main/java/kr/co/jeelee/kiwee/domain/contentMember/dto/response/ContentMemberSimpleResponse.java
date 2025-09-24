package kr.co.jeelee.kiwee.domain.contentMember.dto.response;

import java.time.LocalDateTime;

import kr.co.jeelee.kiwee.domain.content.dto.response.ContentSimpleResponse;
import kr.co.jeelee.kiwee.domain.contentMember.entity.ContentMember;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;

public record ContentMemberSimpleResponse(
	ContentSimpleResponse content, MemberSimpleResponse member, LocalDateTime startAt,
	Integer completedCount, Integer recommended, String recommendReason, Double star, Long consumedAmount,
	LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static ContentMemberSimpleResponse from(ContentMember contentMember) {
		return new ContentMemberSimpleResponse(
			ContentSimpleResponse.from(contentMember.getContent()),
			MemberSimpleResponse.from(contentMember.getMember()),
			contentMember.getStartAt(),
			contentMember.getCompletedCount(),
			contentMember.getRecommended(),
			contentMember.getRecommendReason(),
			contentMember.getStar(),
			contentMember.getConsumedAmount(),
			contentMember.getUpdatedAt(),
			contentMember.getCreatedAt()
		);
	}
}
