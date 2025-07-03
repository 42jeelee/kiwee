package kr.co.jeelee.kiwee.domain.quest.entity;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.global.converter.IntegerListToStringConverter;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.model.TermType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quests")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quest extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

	@Column(nullable = false)
	private String icon;

	@Column(nullable = false)
	private String banner;

	@Column(length = 100, nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id")
	private Channel channel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proposer_id")
	private Member proposer;

	@Column(nullable = false)
	private LocalTime verifiableFrom;

	@Column(nullable = false)
	private LocalTime verifiableUntil;

	@Column(nullable = false)
	private Boolean isThisInstant;

	@JdbcTypeCode(SqlTypes.BIGINT)
	@Column
	private Duration completedLimit;

	@Column(nullable = false)
	private Integer maxSuccess;

	@Column(nullable = false)
	private Integer maxProgressCount;

	@Column
	private Integer maxRetryAllowed;

	@Column(nullable = false)
	private Boolean isActive;

	@Column(nullable = false)
	private TermType termType;

	@Column
	@Convert(converter = IntegerListToStringConverter.class)
	private List<Integer> activeDays;

	@Column(nullable = false)
	private Integer minPerTerm;

	@Column(nullable = false)
	private Integer skipTerm;

	@Column(nullable = false)
	private Integer maxSkipTerm;

	@Column
	private Integer maxAllowedFails;

	private Quest(
		String icon, String banner, String title, String description,
		Channel channel, Member proposer, LocalTime verifiableFrom, LocalTime verifiableUntil,
		Boolean isThisInstant, Duration completedLimit, Integer maxSuccess, Integer maxProgressCount,
		Integer maxRetryAllowed, Boolean isActive, TermType termType, List<Integer> activeDays,
		Integer minPerTerm, Integer maxSkipTerm, Integer maxAllowedFails
	) {
		this.icon = icon;
		this.banner = banner;
		this.title = title;
		this.description = description;
		this.channel = channel;
		this.proposer = proposer;
		this.verifiableFrom = verifiableFrom;
		this.verifiableUntil = verifiableUntil;
		this.isThisInstant = isThisInstant;
		this.completedLimit = completedLimit;
		this.maxSuccess = maxSuccess;
		this.maxProgressCount = maxProgressCount;
		this.maxRetryAllowed = maxRetryAllowed;
		this.isActive = isActive;
		this.termType = termType;
		this.activeDays = activeDays;
		this.minPerTerm = minPerTerm;
		this.skipTerm = maxSkipTerm;
		this.maxSkipTerm = maxSkipTerm;
		this.maxAllowedFails = maxAllowedFails;
	}

	public static Quest of(
		String icon, String banner, String title, String description,
		Channel channel, Member proposer, LocalTime verifiableFrom, LocalTime verifiableUntil,
		Boolean isThisInstant, Duration completedLimit, Integer maxSuccess, Integer maxProgressCount,
		Integer maxRetryAllowed, Boolean isActive, TermType termType, List<Integer> activeDays,
		Integer minPerTerm, Integer maxSkipTerm, Integer maxAllowedFails
	) {
		return new Quest(
			icon, banner, title, description, channel, proposer, verifiableFrom, verifiableUntil,
			isThisInstant, completedLimit, maxSuccess, maxProgressCount, maxRetryAllowed, isActive,
			termType, activeDays, minPerTerm, maxSkipTerm, maxAllowedFails
		);
	}

	public void updateTitle(String title) {
		if (title == null || title.isBlank()) {
			throw new FieldValidationException("title", "제목은 비어있을 수 없습니다.");
		}
		if (title.length() > 100) {
			throw new FieldValidationException("title", "제목은 최대 100자까지 가능합니다.");
		}
		this.title = title;
	}

	public void updateIcon(String icon) {
		validateImageUrl("icon", icon);
		this.icon = icon;
	}

	public void updateBanner(String banner) {
		validateImageUrl("banner", banner);
		this.banner = banner;
	}

	public void updateDescription(String description) {
		this.description = description;
	}

	public void isThisInstant(Boolean isThisInstant) {
		this.isThisInstant = isThisInstant;
	}

	public void updateVerifiableTerm(LocalTime verifiableFrom, LocalTime verifiableUntil) {
		if (Objects.equals(verifiableFrom == null, verifiableUntil == null)) {
			if (verifiableFrom != null && verifiableFrom.isAfter(verifiableUntil)) {
				throw new FieldValidationException("verifiableUtil", "verifiableUntil은 verifiableFrom 보다 앞에 있을 수 없습니다.");
			}
			this.verifiableFrom = verifiableFrom;
			this.verifiableUntil = verifiableUntil;
			return;
		}

		throw new FieldValidationException("verifiableFrom", "verifiableFrom, verifiableUntil 값은 둘 다 있거나 없어야 합니다.");
	}

	public void updateCompletedLimit(Duration completedLimit) {
		this.completedLimit = completedLimit;
	}

	public void updateMaxSuccess(Integer maxSuccess) {
		if (maxSuccess == null || maxSuccess < 0) {
			throw new FieldValidationException("maxSuccess", "빈 값 이거나 0보다 작을 수 없습니다.");
		}
		this.maxSuccess = maxSuccess;
	}

	public void updateMaxProgressCount(Integer maxProgressCount) {
		if (maxProgressCount == null || maxProgressCount < 0) {
			throw new FieldValidationException("maxProgressCount", "빈 값 이거나 0보다 작을 수 없습니다.");
		}
		this.maxProgressCount = maxProgressCount;
	}

	public void updateMaxRetryAllowed(Integer maxRetryAllowed) {
		if (maxRetryAllowed < 0) {
			throw new FieldValidationException("maxRetryAllowed", "0보다 작을 수 없습니다.");
		}
		this.maxRetryAllowed = maxRetryAllowed;
	}

	public void isActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public void updateMinPerTerm(Integer minPerTerm) {
		if (minPerTerm == null || minPerTerm < 0) {
			throw new FieldValidationException("minPerTerm", "빈 값 이거나 0보다 작을 수 없습니다.");
		}
		this.minPerTerm = minPerTerm;
	}

	public void updateSchedule(
		TermType termType,
		List<Integer> activeDays,
		Integer maxSkipTerm
	) {
		if (termType == null) {
			throw new FieldValidationException("termType", "빈 값이거나 잘못된 값이 들어갔습니다.");
		}

		this.termType = termType;
		if (termType.equals(TermType.NONE)) {
			this.activeDays = null;
			this.maxSkipTerm = 0;
		} else {
			if (maxSkipTerm == null || maxSkipTerm < 0) {
				throw new FieldValidationException("maxSkipTerm", "빈 값 이거나 0보다 작을 수 없습니다.");
			}
			this.activeDays = activeDays;
			this.maxSkipTerm = maxSkipTerm;
		}

	}

	public boolean shouldSkipThisTerm() {
		if (this.skipTerm > 0) {
			this.skipTerm--;
			return true;
		}
		this.skipTerm = this.maxSkipTerm;
		return false;
	}

	private void validateImageUrl(String fieldName, String url) {
		final Pattern IMAGE_PATTERN = Pattern.compile(
			"^(https?://)([\\w\\-]+\\.)+[\\w\\-]+(/[\\w\\-./?%&=]*)?\\.(jpg|jpeg|png|gif|bmp|svg)$"
		);

		if (url == null || url.isBlank()) {
			throw new FieldValidationException(fieldName, "사진 URL은 비어있을 수 없습니다.");
		}
		if (!IMAGE_PATTERN.matcher(url).matches()) {
			throw new FieldValidationException(fieldName, "사진 URL 형식이 아닙니다.");
		}
	}

}
