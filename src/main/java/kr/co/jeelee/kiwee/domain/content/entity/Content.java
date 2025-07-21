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
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.domain.genre.entity.Genre;
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
	private String title;

	@Column(nullable = false, length = 3000)
	private String overview;

	@Column(nullable = false)
	private Double rating;

	@Column
	private String imageUrl;

	@Column
	private String homepage;

	@Column(nullable = false)
	private ContentType contentType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Content parent;

	@ManyToMany
	@JoinTable(
		name = "content_genres",
		joinColumns = @JoinColumn(name = "content_id"),
		inverseJoinColumns = @JoinColumn(name = "genre_id")
	)
	private Set<Genre> genres;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Content> children;

	private Content(
		String title, String overview, Double rating,  String imageUrl,
		String homepage, ContentType contentType, Content parent, Set<Genre> genres
	) {
		this.title = title;
		this.overview = overview;
		this.rating = rating;
		this.imageUrl = imageUrl;
		this.homepage = homepage;
		this.contentType = contentType;
		this.parent = parent;
		this.genres = genres;
		this.children = new ArrayList<>();
	}

	public static Content of(
		String title, String overview, Double rating,  String imageUrl,
		String homepage, ContentType contentType, Content parent, Set<Genre> genres
	) {
		return new Content(
			title, overview, rating, imageUrl, homepage, contentType, parent, genres
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

	public void updateHomepage(String homepage) {
		this.homepage = homepage;
	}

	public void updateContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public void updateParent(Content parent) {
		this.parent = parent;
	}

	public void updateGenres(Set<Genre> genres) {
		this.genres = genres;
	}

}
