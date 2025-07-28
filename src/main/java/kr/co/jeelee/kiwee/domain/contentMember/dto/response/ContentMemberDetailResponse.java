package kr.co.jeelee.kiwee.domain.contentMember.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import kr.co.jeelee.kiwee.domain.content.dto.response.ContentSimpleResponse;
import kr.co.jeelee.kiwee.domain.contentMember.entity.ContentMember;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.review.dto.response.ReviewSimpleResponse;

public record ContentMemberDetailResponse(
	ContentSimpleResponse content, MemberSimpleResponse member, LocalDateTime startAt,
	Integer completedCount, Integer recommended, String recommendReason, Double star,
	Long consumedAmount, List<ReviewSimpleResponse> reviews,
	LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static ContentMemberDetailResponse from(ContentMember contentMember) {
		return new ContentMemberDetailResponse(
			ContentSimpleResponse.from(contentMember.getContent()),
			MemberSimpleResponse.from(contentMember.getMember()),
			contentMember.getStartAt(),
			contentMember.getCompletedCount(),
			contentMember.getRecommended(),
			contentMember.getRecommendReason(),
			contentMember.getStar(),
			contentMember.getConsumedAmount(),
			contentMember.getReviews() != null
				? contentMember.getReviews().stream()
					.map(ReviewSimpleResponse::from)
						.toList()
				: null,
			contentMember.getUpdatedAt(),
			contentMember.getCreatedAt()
		);
	}
}
