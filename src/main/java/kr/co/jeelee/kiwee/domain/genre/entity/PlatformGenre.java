package kr.co.jeelee.kiwee.domain.genre.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "platform_genres")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlatformGenre extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "platform_id")
	private Platform platform;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "genre_id")
	private Genre genre;

	@Column(nullable = false)
	private String idInPlatform;

	@Column(nullable = false)
	private String originalName;

	private PlatformGenre(Platform platform, Genre genre, String idInPlatform, String originalName) {
		this.platform = platform;
		this.genre = genre;
		this.idInPlatform = idInPlatform;
		this.originalName = originalName;
	}

	public static PlatformGenre of(Platform platform, Genre genre, String idInPlatform, String originalName) {
		return new PlatformGenre(platform, genre, idInPlatform, originalName);
	}

}
