package kr.co.jeelee.kiwee.domain.contentMember.service;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.domain.contentMember.dto.request.ContentMemberCreateRequest;
import kr.co.jeelee.kiwee.domain.contentMember.dto.request.ContentMemberUpdateRequest;
import kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberStarResponse;
import kr.co.jeelee.kiwee.domain.contentMember.entity.ContentMember;
import kr.co.jeelee.kiwee.domain.review.dto.request.ReviewCreateRequest;
import kr.co.jeelee.kiwee.domain.review.entity.Review;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface ContentMemberService {

	ContentMemberDetailResponse createContentMember(UUID contentId, UUID memberId, ContentMemberCreateRequest request);

	ContentMemberDetailResponse getContentMember(UUID contentId, UUID memberId);

	PagedResponse<ContentMemberSimpleResponse> getContentMembersByContentId(UUID contentId, boolean includeChild, Pageable pageable);

	PagedResponse<ContentMemberSimpleResponse> getContentMembersByMemberId(UUID memberId, Set<ContentType> contentTypes, Pageable pageable);

	ContentMemberDetailResponse updateContentMember(UUID contentId, UUID memberId, ContentMemberUpdateRequest request);

	void deleteContentMember(UUID contentId, UUID memberId);

	double getCompletedRate(UUID contentId, UUID memberId);

	ContentMember getOrCreateByReviewCreate(UUID contentId, UUID memberId, ReviewCreateRequest request);

	ContentMember getByContentIdAndMemberId(UUID contentId, UUID memberId);

	void updateByReviews(UUID contentId, UUID memberId, Review review);

	ContentMemberStarResponse getAverageStar(UUID contentId);

}
