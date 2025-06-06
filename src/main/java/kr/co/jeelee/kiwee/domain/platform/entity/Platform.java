package kr.co.jeelee.kiwee.domain.platform.entity;

import java.util.UUID;
import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "platforms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Platform extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(length = 30, nullable = false, unique = true)
	private String name;

	@Column(length = 255, nullable = false)
	private String icon;

	@Column(length = 255, nullable = false)
	private String banner;

	@Column
	private String description;

	@Column(length = 500)
	private String page;

	@Column(length = 50)
	private String provider;

	@Column(nullable = false)
	private boolean isToken;

	private Platform(String name, String icon, String banner, String description, String page, String provider, boolean isToken) {
		this.name = name;
		this.icon = icon;
		this.banner = banner;
		this.description = description;
		this.page = page;
		this.provider = provider;
		this.isToken = isToken;
	}

	public static Platform of(String name, String icon, String banner, String description, String page, String provider, boolean isToken) {
		return new Platform(name, icon, banner, description, page, provider, isToken);
	}

	public void updateName(String name) {
		if (name == null || name.isBlank()) {
			throw new FieldValidationException("name", "이름은 비어있을 수 없습니다.");
		}
		if (name.length() > 30) {
			throw new FieldValidationException("name", "이름은 최대 30자까지 가능합니다.");
		}
		this.name = name.trim();
	}

	public void updateIcon(String icon) {
		final Pattern IMAGE_PATTERN = Pattern.compile(
			"^(https?://)([\\w\\-]+\\.)+[\\w\\-]+(/[\\w\\-./?%&=]*)?\\.(jpg|jpeg|png|gif|bmp|svg)$"
		);

		if (icon == null || icon.isBlank()) {
			throw new FieldValidationException("icon", "이미지는 비어있을 수 없습니다.");
		}
		if (!IMAGE_PATTERN.matcher(icon).matches()) {
			throw new FieldValidationException("icon", "이미지 URL 형식이 아닙니다.");
		}
		this.icon = icon;
	}

	public void updateBanner(String banner) {
		final Pattern IMAGE_PATTERN = Pattern.compile(
			"^(https?://)([\\w\\-]+\\.)+[\\w\\-]+(/[\\w\\-./?%&=]*)?\\.(jpg|jpeg|png|gif|bmp|svg)$"
		);

		if (banner == null || banner.isBlank()) {
			throw new FieldValidationException("banner", "이미지는 비어있을 수 없습니다.");
		}

		if (!IMAGE_PATTERN.matcher(banner).matches()) {
			throw new FieldValidationException("banner", "이미지 URL 형식이 아닙니다.");
		}
		this.banner = banner;
	}

	public void updateDescription(String description) {
		this.description = description != null ? description.trim() : null;
	}

	public void updatePage(String page) {
		this.page = page != null ? page.trim() : null;
	}

	public void updateProvider(String provider) {
		this.provider = provider != null ? provider.trim() : null;
	}

	public void updateToken(boolean isToken) {
		this.isToken = isToken;
	}

}
