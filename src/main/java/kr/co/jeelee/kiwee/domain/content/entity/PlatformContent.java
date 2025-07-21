package kr.co.jeelee.kiwee.domain.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "platform_contents",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_platform_content_id", columnNames = {"platform_id", "id_in_platform"})
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlatformContent extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "platform_id")
	private Platform platform;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id")
	private Content content;

	@Column(nullable = false)
	private String idInPlatform;

	private PlatformContent(Platform platform, Content content, String idInPlatform) {
		this.platform = platform;
		this.content = content;
		this.idInPlatform = idInPlatform;
	}

	public static PlatformContent of(Platform platform, Content content, String idInPlatform) {
		return new PlatformContent(platform, content, idInPlatform);
	}

}
