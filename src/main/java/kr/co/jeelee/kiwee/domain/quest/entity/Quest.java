package kr.co.jeelee.kiwee.domain.quest.entity;

import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import jakarta.persistence.Column;
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

	@Column(nullable = false)
	private Boolean isRepeatable;

	@Column(nullable = false)
	private Integer maxSuccess;

	@Column(nullable = false)
	private Integer maxRetryAllowed;

	@Column(nullable = false)
	private Boolean isActive;

	@Column(nullable = false)
	private Boolean autoReschedule;

	@Column(nullable = false)
	private TermType rescheduleTerm;

	private Quest(
		String icon, String banner, String title, String description, Channel channel,
		Member proposer, LocalTime verifiableFrom, LocalTime verifiableUntil, Boolean isThisInstant,
		Boolean isRepeatable, Integer maxSuccess, Integer maxRetryAllowed, Boolean isActive,
		Boolean autoReschedule, TermType rescheduleTerm
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
		this.isRepeatable = isRepeatable;
		this.maxSuccess = maxSuccess;
		this.maxRetryAllowed = maxRetryAllowed;
		this.isActive = isActive;
		this.autoReschedule = autoReschedule;
		this.rescheduleTerm = rescheduleTerm;
	}

	public static Quest of(
		String icon, String banner, String title, String description, Channel channel,
		Member proposer, LocalTime verifiableFrom, LocalTime verifiableUntil, Boolean isThisInstant,
		Boolean isRepeatable, Integer maxSuccess, Integer maxRetryAllowed, Boolean isActive,
		Boolean autoReschedule, TermType rescheduleTerm
	) {
		return new Quest(
			icon, banner, title, description, channel, proposer, verifiableFrom, verifiableUntil, isThisInstant,
			isRepeatable, maxSuccess, maxRetryAllowed, isActive, autoReschedule, rescheduleTerm
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

	public void isRepeatable(Boolean isRepeatable) {
		this.isRepeatable = isRepeatable;
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

	public void updateMaxSuccess(Integer maxSuccess) {
		if (maxSuccess == null || maxSuccess < 0) {
			throw new FieldValidationException("maxSuccess", "빈 값 이거나 0보다 작을 수 없습니다.");
		}
		this.maxSuccess = maxSuccess;
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

	public void isAutoReschedule(TermType rescheduleTerm) {
		this.autoReschedule = true;
		this.rescheduleTerm = rescheduleTerm;
	}

	public void isNotAutoReschedule() {
		this.autoReschedule = false;
		this.rescheduleTerm = TermType.NONE;
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
