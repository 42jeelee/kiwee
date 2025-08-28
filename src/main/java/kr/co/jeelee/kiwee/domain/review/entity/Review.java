package kr.co.jeelee.kiwee.domain.review.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.contentMember.entity.ContentMember;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_member_id")
	private ContentMember contentMember;

	@Lob
	@Column(nullable = false)
	private String message;

	@Column(nullable = false)
	private Double star;

	@Column(nullable = false)
	private Integer completedCount;

	@Column
	private Long consumedAmount;

	@Column(nullable = false)
	private Boolean isSpoiler;

	private Review(
		ContentMember contentMember, String message, Double star,
		Integer completedCount, Long consumedAmount, Boolean isSpoiler
	) {
		this.contentMember = contentMember;
		this.message = message;
		this.star = star;
		this.completedCount = completedCount;
		this.consumedAmount = consumedAmount;
		this.isSpoiler = isSpoiler;
	}

	public static Review of(
		ContentMember contentMember, String message, Double star,
		Integer completedCount, Long consumedAmount, Boolean isSpoiler
	) {
		return new Review(contentMember, message, star, completedCount, consumedAmount, isSpoiler);
	}

	public void updateMessage(String message) {
		if (message == null || message.isBlank()) {
			throw new FieldValidationException("message", "메세지는 비어있을 수 잆습니다.");
		}
		this.message = message;
	}

	public void updateStar(double star) {
		if (star < -10 || 10 < star) {
			throw new FieldValidationException("star", "별점은 절댓값 10이하여야 합니다.");
		}
		this.star = star;
	}

	public void updateConsumedAmount(long consumedAmount) {
		if (consumedAmount < 0) {
			throw new FieldValidationException("consumedAmount", "소비량은 음수일 수 없습니다.");
		}
		this.consumedAmount = consumedAmount;
	}

	public void updateSpoiler(boolean isSpoiler) {
		this.isSpoiler = isSpoiler;
	}

}
