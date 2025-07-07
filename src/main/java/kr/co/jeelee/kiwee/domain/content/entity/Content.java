package kr.co.jeelee.kiwee.domain.content.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

	private Content(
		DataProvider sourceProvider, String sourceId, String title, String originalTitle,
		String description, Double rating, String imageUrl, String homePage,
		ContentType contentType, ContentLevel contentLevel, Content parent,
		Set<Genre> genres, Set<Platform> platforms
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
	}

	public static Content of(
		DataProvider sourceProvider, String sourceId, String title, String originalTitle,
		String description, Double rating, String imageUrl, String homePage,
		ContentType contentType, ContentLevel contentLevel, Content parent,
		Set<Genre> genres, Set<Platform> platforms
	) {
		return new Content(
			sourceProvider, sourceId, title, originalTitle, description, rating,
			imageUrl, homePage, contentType, contentLevel, parent,
			genres, platforms
		);
	}

}
