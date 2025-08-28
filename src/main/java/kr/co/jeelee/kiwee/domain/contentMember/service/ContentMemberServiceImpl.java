package kr.co.jeelee.kiwee.domain.contentMember.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.domain.content.service.ContentService;
import kr.co.jeelee.kiwee.domain.contentMember.dto.request.ContentMemberCreateRequest;
import kr.co.jeelee.kiwee.domain.contentMember.dto.request.ContentMemberUpdateRequest;
import kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberStarResponse;
import kr.co.jeelee.kiwee.domain.contentMember.entity.ContentMember;
import kr.co.jeelee.kiwee.domain.contentMember.exception.ContentMemberCantUpdateException;
import kr.co.jeelee.kiwee.domain.contentMember.exception.ContentMemberNotFoundException;
import kr.co.jeelee.kiwee.domain.contentMember.repository.ContentMemberRepository;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.memberActivity.event.MemberActivityEvent;
import kr.co.jeelee.kiwee.domain.review.dto.request.ReviewCreateRequest;
import kr.co.jeelee.kiwee.domain.review.entity.Review;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.model.PermissionType;
import kr.co.jeelee.kiwee.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentMemberServiceImpl implements ContentMemberService {

	private final ContentMemberRepository contentMemberRepository;

	private final ContentService contentService;
	private final MemberService memberService;

	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public ContentMemberDetailResponse createContentMember(UUID contentId, UUID memberId,
		ContentMemberCreateRequest request) {
		Content content = contentService.getById(contentId);
		Member member = memberService.getById(memberId);

		if (!SecurityUtil.isMemberSelfOrHasPrivilege(member.getId(), PermissionType.ROLE_CREATE_OTHER_TASK)) {
			throw new AccessDeniedException();
		}

		int recommended = request.recommended() != null ? request.recommended() : 0;

		if (recommended != 0 && request.recommendReason() == null) {
			throw new FieldValidationException("recommendReason", "추천/비추천에는 이유가 있어야 합니다.");
		}

		ContentMember contentMember = ContentMember.of(
			content,
			member,
			request.startAt(),
			recommended,
			request.recommendReason(),
			request.star(),
			request.consumedAmount()
		);

		contentMember.updateCompleteCount(request.isCompleted());

		ContentMember savedContentMember = contentMemberRepository.save(contentMember);

		if (request.isCompleted()) {
			eventPublisher.publishEvent(MemberActivityEvent.of(
				member.getId(),
				DomainType.CONTENT,
				content.getId(),
				ActivityType.COMPLETE
			));
		}

		return ContentMemberDetailResponse.from(savedContentMember);
	}

	@Override
	public ContentMemberDetailResponse getContentMember(UUID contentId, UUID memberId) {
		return ContentMemberDetailResponse.from(getByContentIdAndMemberId(contentId, memberId));
	}

	@Override
	public PagedResponse<ContentMemberSimpleResponse> getContentMembersByContentId(UUID contentId, Pageable pageable) {
		Content content = contentService.getById(contentId);

		return PagedResponse.of(
			contentMemberRepository.findByContent(content, pageable),
			ContentMemberSimpleResponse::from
		);
	}

	@Override
	public PagedResponse<ContentMemberSimpleResponse> getContentMembersByMemberId(UUID memberId, Set<ContentType> contentTypes,
		Pageable pageable) {
		Member member = memberService.getById(memberId);

		return PagedResponse.of(
			contentTypes != null && !contentTypes.isEmpty() ?
				contentMemberRepository.findByMemberAndContent_ContentTypeIn(member, contentTypes, pageable)
				: contentMemberRepository.findByMember(member, pageable),
			ContentMemberSimpleResponse::from
		);
	}

	@Override
	@Transactional
	public ContentMemberDetailResponse updateContentMember(UUID contentId, UUID memberId,
		ContentMemberUpdateRequest request) {
		ContentMember contentMember = getByContentIdAndMemberId(contentId, memberId);

		if (!SecurityUtil.isMemberSelfOrHasPrivilege(memberId, PermissionType.ROLE_EDIT_OTHER_TASK)) {
			throw new AccessDeniedException();
		}

		if (
			(request.startAt() != null || request.star() != null || request.consumedAmount() != null) &&
			contentMember.getReviews() != null && !contentMember.getReviews().isEmpty()
		) {
			throw new ContentMemberCantUpdateException();
		}

		updateCompleteCountIfChanged(contentMember, request.isCompleted());
		updateRecommendedIfChanged(contentMember, request.recommended(), request.recommendReason());
		updateStartIfChanged(contentMember, request.startAt());
		updateStarIfChanged(contentMember, request.star());

		return ContentMemberDetailResponse.from(contentMember);
	}

	@Override
	@Transactional
	public void deleteContentMember(UUID contentId, UUID memberId) {
		if (!SecurityUtil.isMemberSelfOrHasPrivilege(memberId, PermissionType.ROLE_DELETE_OTHER_TASK)) {
			throw new AccessDeniedException();
		}

		contentMemberRepository.delete(getByContentIdAndMemberId(contentId, memberId));
	}

	@Override
	public double getCompletedRate(UUID contentId, UUID memberId) {
		ContentMember contentMember = getByContentIdAndMemberId(contentId, memberId);
		Long totalAmount = contentMember.getContent().getTotalAmount();

		if (totalAmount == null || totalAmount == 0) {
			return 0;
		}

		long consumedAmount = contentMember.getConsumedAmount() != null
			? contentMember.getConsumedAmount()
			: 0;

		double sum = consumedAmount * 100;
		if (contentMemberRepository.existsByContent_Parent_IdAndMember_Id(contentId, memberId)) {
			List<ContentMember> children = contentMemberRepository
					.findByContent_Parent_IdAndMember_IdAndContent_ChildrenIdxGreaterThan(
						contentId,
						memberId,
						consumedAmount
					);

			sum += children.stream()
				.mapToDouble(this::calcChildrenCompletedRate)
				.sum();
		}

		return sum / totalAmount;
	}

	private double calcChildrenCompletedRate(ContentMember cm) {
		Long totalAmount = cm.getContent().getTotalAmount();

		if (totalAmount == null || totalAmount == 0) {
			return 0;
		}

		long consumedAmount = cm.getCompletedCount() > 0
			? totalAmount
			: cm.getConsumedAmount() != null
				? cm.getConsumedAmount()
				: 0;

		double sum = consumedAmount * 100;
		if (contentMemberRepository.existsByContent_Parent_IdAndMember_Id(cm.getContent().getId(), cm.getMember().getId())) {
			List<ContentMember> children = contentMemberRepository
				.findByContent_Parent_IdAndMember_IdAndContent_ChildrenIdxGreaterThan(
					cm.getContent().getId(),
					cm.getMember().getId(),
					consumedAmount
				);

			sum += children.stream()
				.mapToDouble(this::calcChildrenCompletedRate)
				.sum();
		}

		return sum / totalAmount;
	}

	@Override
	@Transactional
	public ContentMember getOrCreateByReviewCreate(UUID contentId, UUID memberId, ReviewCreateRequest request) {
		Content content = contentService.getById(contentId);
		Member member = memberService.getById(memberId);

		return contentMemberRepository.findByContentAndMember(content, member)
			.orElseGet(() -> {
				Content root = contentService.getRootById(content.getId());

				if (
					root.getId() != content.getId() &&
					!contentMemberRepository.existsByMemberIdAndContentId(member.getId(), content.getId())
				) {
					contentMemberRepository.save(
						ContentMember.of(
							root,
							member,
							LocalDateTime.now(),
							0,
							null,
							request.star(),
							0L
						)
					);
				}

				return contentMemberRepository.save(
					ContentMember.of(
						content,
						member,
						LocalDateTime.now(),
						0,
						null,
						request.star(),
						request.consumedAmount()
					)
				);
			});
	}

	@Override
	public ContentMember getByContentIdAndMemberId(UUID contentId, UUID memberId) {
		Content content = contentService.getById(contentId);
		Member member = memberService.getById(memberId);

		return contentMemberRepository.findByContentAndMember(content, member)
			.orElseThrow(ContentMemberNotFoundException::new);
	}

	@Override
	@Transactional
	public void updateByReviews(UUID contentId, UUID memberId, Review review) {
		ContentMember contentMember = getByContentIdAndMemberId(contentId, memberId);
		List<Review> reviews = contentMember.getReviews() != null
			? contentMember.getReviews()
			: new ArrayList<>();

		reviews.add(review);

		double star = reviews.stream()
			.map(Review::getStar)
			.filter(Objects::nonNull)
			.mapToDouble(Double::doubleValue)
			.average()
			.orElse(0d);

		long consumedAmount = reviews.stream()
			.filter(r -> r.getCompletedCount().equals(review.getCompletedCount()))
			.map(Review::getConsumedAmount)
			.filter(Objects::nonNull)
			.mapToLong(Long::longValue)
			.max()
			.orElse(0L);

		contentMember.updateStar(star);
		contentMember.updateConsumedAmount(consumedAmount);
	}

	@Override
	public ContentMemberStarResponse getAverageStar(UUID contentId) {
		return contentMemberRepository.getAverageStarByContentId(contentId);
	}

	private void updateStartIfChanged(ContentMember contentMember, LocalDateTime startAt) {
		if (startAt != null && !startAt.equals(contentMember.getStartAt())) {
			contentMember.updateStart(startAt);
		}
	}

	private void updateCompleteCountIfChanged(ContentMember contentMember, Boolean isCompleted) {
		if (isCompleted != null) {
			contentMember.updateCompleteCount(isCompleted);
		}
	}

	private void updateRecommendedIfChanged(ContentMember contentMember, Integer recommended, String recommendReason) {
		if (recommended != null && recommendReason != null && !recommendReason.isBlank()) {
			contentMember.updateRecommended(recommended, recommendReason);
		}
	}

	private void updateStarIfChanged(ContentMember contentMember, Double star) {
		if (star != null && !star.equals(contentMember.getStar())) {
			contentMember.updateStar(star);
		}
	}
}
