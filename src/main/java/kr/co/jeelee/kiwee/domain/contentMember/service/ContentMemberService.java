package kr.co.jeelee.kiwee.domain.contentMember.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.contentMember.dto.request.ContentMemberCreateRequest;
import kr.co.jeelee.kiwee.domain.contentMember.dto.request.ContentMemberUpdateRequest;
import kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.contentMember.entity.ContentMember;
import kr.co.jeelee.kiwee.domain.review.dto.request.ReviewCreateRequest;
import kr.co.jeelee.kiwee.domain.review.entity.Review;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface ContentMemberService {

	ContentMemberDetailResponse createContentMember(UUID contentId, UUID memberId, ContentMemberCreateRequest request);

	ContentMemberDetailResponse getContentMember(UUID contentId, UUID memberId);

	PagedResponse<ContentMemberSimpleResponse> getContentMembers(UUID contentId, UUID memberId, Pageable pageable);

	ContentMemberDetailResponse updateContentMember(UUID contentId, UUID memberId, ContentMemberUpdateRequest request);

	void deleteContentMember(UUID contentId, UUID memberId);

	ContentMember getOrCreateByReviewCreate(UUID contentId, UUID memberId, ReviewCreateRequest request);

	ContentMember getByContentIdAndMemberId(UUID contentId, UUID memberId);

	void updateByReviews(UUID contentId, UUID memberId, Review review);

}
