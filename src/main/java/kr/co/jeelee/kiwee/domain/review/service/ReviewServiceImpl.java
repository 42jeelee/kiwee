package kr.co.jeelee.kiwee.domain.review.service;

import java.util.List;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.contentMember.entity.ContentMember;
import kr.co.jeelee.kiwee.domain.contentMember.service.ContentMemberService;
import kr.co.jeelee.kiwee.domain.memberActivity.event.MemberActivityEvent;
import kr.co.jeelee.kiwee.domain.review.dto.request.ReviewCreateRequest;
import kr.co.jeelee.kiwee.domain.review.dto.request.ReviewUpdateRequest;
import kr.co.jeelee.kiwee.domain.review.dto.response.ReviewDetailResponse;
import kr.co.jeelee.kiwee.domain.review.dto.response.ReviewSimpleResponse;
import kr.co.jeelee.kiwee.domain.review.entity.Review;
import kr.co.jeelee.kiwee.domain.review.exception.ReviewNotFoundException;
import kr.co.jeelee.kiwee.domain.review.repository.ReviewRepository;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.model.PermissionType;
import kr.co.jeelee.kiwee.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;

	private final ContentMemberService contentMemberService;

	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public ReviewDetailResponse review(UUID contentId, UUID memberId, ReviewCreateRequest request) {
		if (!SecurityUtil.isMemberSelfOrHasPrivilege(memberId, PermissionType.ROLE_CREATE_OTHER_TASK)) {
			throw new AccessDeniedException();
		}

		ContentMember contentMember = contentMemberService.getOrCreateByReviewCreate(contentId, memberId, request);

		Review review = Review.of(
			contentMember,
			request.message(),
			request.star(),
			contentMember.getCompletedCount(),
			request.consumedAmount()
		);

		Review savedReview = reviewRepository.save(review);

		contentMemberService.updateByReviews(contentId, memberId, savedReview);

		eventPublisher.publishEvent(MemberActivityEvent.of(
			memberId,
			DomainType.CONTENT,
			contentMember.getContent().getId(),
			ActivityType.RECORD
		));

		Long totalAmount = contentMember.getContent().getTotalAmount();
		Long consumedAmount = savedReview.getConsumedAmount();

		if (
			totalAmount != null &&
			consumedAmount != null &&
			totalAmount <= consumedAmount
		) {
			contentMember.updateCompleteCount(true);

			eventPublisher.publishEvent(MemberActivityEvent.of(
				memberId,
				DomainType.CONTENT,
				contentMember.getContent().getId(),
				ActivityType.COMPLETE
			));
		}

		return ReviewDetailResponse.from(savedReview);
	}

	@Override
	public PagedResponse<ReviewDetailResponse> getReviewDetails(UUID contentId, UUID memberId, Pageable pageable) {
		ContentMember contentMember = contentMemberService.getByContentIdAndMemberId(contentId, memberId);

		return PagedResponse.of(
			reviewRepository.findByContentMember(contentMember, pageable),
			ReviewDetailResponse::from
		);
	}

	@Override
	public PagedResponse<ReviewSimpleResponse> getReviews(UUID contentId, UUID memberId, Pageable pageable) {
		return PagedResponse.of(
			fetchReviews(contentId, memberId, pageable),
			ReviewSimpleResponse::from
		);
	}

	@Override
	public PagedResponse<ReviewSimpleResponse> getReviewsByConsumedAmount(UUID contentId, Long consumedAmount,
		Pageable pageable) {
		return PagedResponse.of(
			reviewRepository.findByContentMember_ContentIdAndConsumedAmount(contentId, consumedAmount, pageable),
			ReviewSimpleResponse::from
		);
	}

	@Override
	@Transactional
	public ReviewDetailResponse updateReview(UUID id, ReviewUpdateRequest request) {
		Review review = getById(id);

		if (!SecurityUtil.isMemberSelfOrHasPrivilege(
			review.getContentMember().getMember().getId(),
			PermissionType.ROLE_EDIT_OTHER_TASK
		)) {
			throw new AccessDeniedException();
		}

		updateMessageIfChanged(review, request.message());
		updateStarIfChanged(review, request.star());
		updateConsumedAmountIfChanged(review, request.consumedAmount());

		return ReviewDetailResponse.from(review);
	}

	@Override
	public List<Long> getReviewConsumedAmountByContentId(UUID contentId) {
		return reviewRepository.findDistinctConsumedAmountByContentId(contentId);
	}

	@Override
	@Transactional
	public void deleteById(UUID id) {
		Review review = getById(id);

		if (!SecurityUtil.isMemberSelfOrHasPrivilege(
			review.getContentMember().getMember().getId(),
			PermissionType.ROLE_DELETE_OTHER_TASK)
		) {
			throw new AccessDeniedException();
		}

		reviewRepository.delete(review);
	}

	@Override
	public Review getById(UUID id) {
		return reviewRepository.findById(id)
			.orElseThrow(ReviewNotFoundException::new);
	}

	private Page<Review> fetchReviews(UUID contentId, UUID memberId, Pageable pageable) {
		if (contentId != null && memberId != null) {
			ContentMember contentMember = contentMemberService.getByContentIdAndMemberId(contentId, memberId);
			return reviewRepository.findByContentMember(contentMember, pageable);
		}

		if (contentId != null) {
			return reviewRepository.findByContentMember_ContentId(contentId, pageable);
		}

		if (memberId != null) {
			return reviewRepository.findByContentMember_MemberId(memberId, pageable);
		}

		return reviewRepository.findAll(pageable);
	}

	private void updateMessageIfChanged(Review review, String message) {
		if (message != null && !message.equals(review.getMessage())) {
			review.updateMessage(message);
		}
	}

	private void updateStarIfChanged(Review review, Double star) {
		if (star != null && !star.equals(review.getStar())) {
			review.updateStar(star);
		}
	}

	private void updateConsumedAmountIfChanged(Review review, Long consumedAmount) {
		if (consumedAmount != null && !consumedAmount.equals(review.getConsumedAmount())) {
			review.updateConsumedAmount(consumedAmount);
		}
	}

}
