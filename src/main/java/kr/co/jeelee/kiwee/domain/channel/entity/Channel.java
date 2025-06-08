package kr.co.jeelee.kiwee.domain.channel.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "channel")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(length = 30, unique = true, nullable = false)
	private String name;

	@Column(nullable = false)
	private String icon;

	@Column(nullable = false)
	private String banner;

	@Column
	private String description;

	@Column(nullable = false)
	private Boolean isOriginal;

	@Column(nullable = false)
	private Boolean isPublic;

	@OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PlatformChannel> platformChannels;

	private Channel(
		String name, String icon, String banner,
		String description, Boolean isOriginal, Boolean isPublic
	) {
		this.name = name;
		this.icon = icon;
		this.banner = banner;
		this.description = description;
		this.isOriginal = isOriginal;
		this.isPublic = isPublic;
		this.platformChannels = new ArrayList<>();
	}

	public static Channel of(
		String name, String icon, String banner,
		String description, Boolean isOriginal, Boolean isPublic
	) {
		return new Channel(name, icon, banner, description, isOriginal, isPublic);
	}

	public void updateName(String name) {
		if (name == null || name.isBlank()) {
			throw new FieldValidationException("name", "이름은 비어있을 수 없습니다.");
		}
		if (name.length() > 30) {
			throw new FieldValidationException("name", "이름은 최대 30자까지 가능합니다.");
		}
		this.name = name;
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

	public void isOriginal() {
		this.isOriginal = true;
	}

	public void isNotOriginal() {
		this.isOriginal = false;
	}

	public void isPublic() {
		this.isPublic = true;
	}

	public void isPrivate() {
		this.isPublic = false;
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
