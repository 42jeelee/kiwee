package kr.co.jeelee.kiwee.domain.auth.entity;

import java.time.LocalDateTime;

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
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;
import kr.co.jeelee.kiwee.global.entity.BaseTimeEntity;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "member_platforms",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_member_platform", columnNames = {"platform_id", "platform_user_id"})
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPlatform extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Platform platform;

	@Column(length = 100, nullable = false)
	private String platformUserId;

	@Column(length = 50, nullable = false)
	private String platformUserName;

	@Column
	private String avatarUrl;

	@Column
	private String email;

	@Column(nullable = false)
	private Boolean isToken;

	@Column
	private String accessToken;

	@Column
	private String refreshToken;

	@Column
	private LocalDateTime tokenExpireAt;

	private MemberPlatform(
		Member member, Platform platform, String platformUserId,
		String platformUserName, String avatarUrl, String email, Boolean isToken,
		String accessToken, String refreshToken, LocalDateTime tokenExpireAt
	) {
		this.member = member;
		this.platform = platform;
		this.platformUserId = platformUserId;
		this.platformUserName = platformUserName;
		this.avatarUrl = avatarUrl;
		this.email = email;
		this.isToken = isToken;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.tokenExpireAt = tokenExpireAt;
	}

	public static MemberPlatform of(
		Member member, Platform platform, String platformUserId,
		String platformUserName, String avatarUrl, String email, Boolean isToken,
		String accessToken, String refreshToken, LocalDateTime tokenExpireAt
	) {
		return new MemberPlatform(
			member, platform, platformUserId, platformUserName,
			avatarUrl, email, isToken, accessToken,
			refreshToken, tokenExpireAt
		);
	}

	public void changeProvideData(String platformUserName, String avatarUrl, String email) {
		if (platformUserName != null && platformUserName.isEmpty() && !platformUserName.equals(this.platformUserName)) {
			this.platformUserName = platformUserName;
		}
		if (avatarUrl != null && avatarUrl.equals(this.avatarUrl)) {
			this.avatarUrl = avatarUrl;
		}
		if (email != null && !email.equals(this.email)) {
			this.email = email;
		}
	}

	public void toggleToken(boolean isToken, String accessToken, String refreshToken, LocalDateTime tokenExpireAt) {
		if (isToken && (accessToken == null || refreshToken == null || tokenExpireAt == null)) {
			throw new FieldValidationException("token", "토큰 정보가 없습니다.");
		}

		this.isToken = isToken;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.tokenExpireAt = tokenExpireAt;
	}

	public void changeOwner(Member member) {
		if (member == null || member.getId().equals(this.member.getId())) {
			throw new FieldValidationException("member", "없는 맴버거나 이미 연결된 맴버입니다.");
		}
		this.member = member;
	}

}
