package kr.co.jeelee.kiwee.domain.contentMember.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.review.entity.Review;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "content_members",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_content_member", columnNames = {"content_id", "member_id"})
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentMember extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", nullable = false)
	private Content content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column
	private LocalDateTime startAt;

	@Column(nullable = false)
	private Integer completedCount;

	@Column(nullable = false)
	private Integer recommended;

	@Column(length = 3000)
	private String recommendReason;

	@Column(nullable = false)
	private Double star;

	@Column
	private Long consumedAmount;

	@OneToMany(mappedBy = "contentMember", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews;

	private ContentMember(
		Content content, Member member, LocalDateTime startAt,
		Integer recommended, String recommendReason, Double star, Long consumedAmount
	) {
		this.content = content;
		this.member = member;
		this.startAt = startAt;
		this.completedCount = 0;
		this.recommended = recommended;
		this.recommendReason = recommendReason;
		this.star = star;
		this.consumedAmount = consumedAmount;
		this.reviews = new ArrayList<>();
	}

	public static ContentMember of(
		Content content, Member member, LocalDateTime startAt,
		Integer isRecommended, String recommendReason, Double star, Long consumedAmount
	) {
		return new ContentMember(
			content, member, startAt, isRecommended, recommendReason, star, consumedAmount
		);
	}

	public void updateStart(LocalDateTime startAt) {
		this.startAt = startAt;
	}

	public void updateCompleteCount(boolean isCompleted) {
		if (isCompleted) {
			this.completedCount += 1;
			this.consumedAmount = 0L;
			return;
		}
		if (this.completedCount > 0) {
			this.completedCount -= 1;
		}
	}

	public void updateRecommended(int recommended, String recommendReason) {
		if (recommendReason == null || recommendReason.isEmpty()) {
			throw new FieldValidationException("recommendReason", "추천/비추천 이유가 있어야 합니다.");
		}
		if (recommended < -2 || 2 < recommended) {
			throw new FieldValidationException("recommended", "추천 계수는 절대값 2를 넘을 수 없습니다.");
		}
		this.recommended = recommended;
		this.recommendReason = recommendReason;
	}

	public void updateStar(Double star) {
		if (star == null) {
			throw new FieldValidationException("star", "별점은 비어있을 수 없습니다.");
		}

		if (star < -10 || 10 < star) {
			throw new FieldValidationException("star", "별점은 절댓값 10이하여야 합니다.");
		}
		this.star = star;
	}

	public void updateConsumedAmount(Long consumedAmount) {
		if (consumedAmount == null) {
			throw new FieldValidationException("consumedAmount", "비어 있을 수 없는 값입니다.");
		}

		if (consumedAmount < 0) {
			throw new FieldValidationException("consumedAmount", "양수만 가능합니다.");
		}
		this.consumedAmount = consumedAmount;
	}

}
