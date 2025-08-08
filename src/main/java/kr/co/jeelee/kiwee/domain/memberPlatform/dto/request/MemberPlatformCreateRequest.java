package kr.co.jeelee.kiwee.domain.memberPlatform.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;

public record MemberPlatformCreateRequest(
	@NotNull(message = "member can't be Null.") Member member,
	@NotNull(message = "platform can't be Null.") Platform platform,
	@NotNull(message = "platformUserId can't be Null.") String platformUserId,
	@NotNull(message = "platformUserName can't be Null.") String platformUserName,
	String avatarUrl, String email, Boolean isToken, String accessToken,
	String refreshToken, LocalDateTime tokenExpireAt
) {
	public static MemberPlatformCreateRequest of(
		Member member, Platform platform,
		String platformUserId, String platformUserName,
		String avatarUrl, String email,
		Boolean isToken, String accessToken,
		String refreshToken, LocalDateTime tokenExpireAt
	) {
		return new MemberPlatformCreateRequest(
			member,
			platform,
			platformUserId,
			platformUserName,
			avatarUrl,
			email,
			isToken,
			accessToken,
			refreshToken,
			tokenExpireAt
		);
	}

	public static MemberPlatformCreateRequest of(
		Member member, Platform platform, String platformUserId,
		String platformUserName, String avatarUrl, String email
	) {
		return MemberPlatformCreateRequest.of(
			member, platform, platformUserId,
			platformUserName, avatarUrl, email,
			false, null, null, null
		);
	}
}
