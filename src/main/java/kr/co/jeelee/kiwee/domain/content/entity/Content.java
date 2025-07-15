package kr.co.jeelee.kiwee.domain.content.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.content.model.ContentLevel;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.model.DataProvider;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.domain.genre.entity.Genre;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private DataProvider sourceProvider;

	@Column
	private String sourceId;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String originalTitle;

	@Column(nullable = false, length = 3000)
	private String description;

	@Column(nullable = false)
	private Double rating;

	@Column
	private String imageUrl;

	@Column
	private String homePage;

	@Column(nullable = false)
	private ContentType contentType;

	@Column(nullable = false)
	private ContentLevel contentLevel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Content parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Content> children;

	@ManyToMany
	@JoinTable(
		name = "content_genres",
		joinColumns = @JoinColumn(name = "content_id"),
		inverseJoinColumns = @JoinColumn(name = "genre_id")
	)
	private Set<Genre> genres;

	@ManyToMany
	@JoinTable(
		name = "content_platforms",
		joinColumns = @JoinColumn(name = "content_id"),
		inverseJoinColumns = @JoinColumn(name = "platform_id")
	)
	private Set<Platform> platforms;

	@Column(unique = true)
	private String applicationId;

	private Content(
		DataProvider sourceProvider, String sourceId, String title, String originalTitle,
		String description, Double rating, String imageUrl, String homePage,
		ContentType contentType, ContentLevel contentLevel, Content parent,
		Set<Genre> genres, Set<Platform> platforms, String applicationId
	) {
		this.sourceProvider = sourceProvider;
		this.sourceId = sourceId;
		this.title = title;
		this.originalTitle = originalTitle;
		this.description = description;
		this.rating = rating;
		this.imageUrl = imageUrl;
		this.homePage = homePage;
		this.contentType = contentType;
		this.contentLevel = contentLevel;
		this.parent = parent;
		this.genres = genres;
		this.platforms = platforms;
		this.children = new ArrayList<>();
		this.applicationId = applicationId;
	}

	public static Content of(
		DataProvider sourceProvider, String sourceId, String title, String originalTitle,
		String description, Double rating, String imageUrl, String homePage,
		ContentType contentType, ContentLevel contentLevel, Content parent,
		Set<Genre> genres, Set<Platform> platforms, String applicationId
	) {
		return new Content(
			sourceProvider, sourceId, title, originalTitle, description, rating,
			imageUrl, homePage, contentType, contentLevel, parent,
			genres, platforms, applicationId
		);
	}

	public void updateTitle(String title) {
		if (title == null || title.isBlank()) {
			throw new FieldValidationException("title", "제목은 비어있을 수 없습니다.");
		}
		this.title = title;
	}

	public void updateDescription(String description) {
		if (description == null || description.isBlank()) {
			throw new FieldValidationException("description", "설명은 비어있을 수 없습니다.");
		}
	}

	public void updateRating(Double rating) {
		if (0 > rating || rating > 5) {
			throw new FieldValidationException("rating", "별점은 0 - 5 사이여야 합니다.");
		}
		this.rating = rating;
	}

	public void updateImageUrl(String imageUrl) {
		final Pattern IMAGE_PATTERN = Pattern.compile(
			"^(https?://)([\\w\\-]+\\.)+[\\w\\-]+(/[\\w\\-./?%&=]*)?\\.(jpg|jpeg|png|gif|bmp|svg)$"
		);

		if (imageUrl == null || imageUrl.isBlank()) {
			throw new FieldValidationException("imageUrl", "사진 URL은 비어있을 수 없습니다.");
		}
		if (!IMAGE_PATTERN.matcher(imageUrl).matches()) {
			throw new FieldValidationException("imageUrl", "사진 URL 형식이 아닙니다.");
		}
		this.imageUrl = imageUrl;
	}

	public void updateHomePage(String homePage) {
		this.homePage = homePage;
	}

	public void updateContentTypeAndLevel(ContentType contentType, ContentLevel contentLevel) {
		ContentType type = contentType != null ? contentType : this.contentType;
		ContentLevel level = contentLevel != null ? contentLevel : this.contentLevel;

		if (
			!(type == ContentType.DRAMA || type == ContentType.ANIMATION)
			&& contentLevel != ContentLevel.ROOT
		) {
			throw new FieldValidationException("contentLevel", "콘텐츠 레벨이 잘못되었습니다.");
		}
		this.contentType = type;
		this.contentLevel = level;
	}

	public void updateGenres(Set<Genre> genres) {
		this.genres = genres;
	}

	public void updatePlatforms(Set<Platform> platforms) {
		this.platforms = platforms;
	}

	public void updateParent(Content parent) {
		if (this.contentLevel != ContentLevel.ROOT) {
			throw new FieldValidationException("parent", "해당 콘텐츠의 상위 레벨은 정의할 수 없습니다.");
		}
		this.parent = parent;
	}

	public void updateApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

}
