package kr.co.jeelee.kiwee.domain.auth.dto.request;

import org.hibernate.validator.constraints.URL;

public record MemberPlatformUpdateDataRequest(
	String platformUserId, String platformUserName,
	@URL(message = "This is not in URL format.") String avatarUrl,
	String email
) {
	public static MemberPlatformUpdateDataRequest of(
		String platformUserId, String platformUserName, String avatarUrl, String email)
	{
		return new MemberPlatformUpdateDataRequest(platformUserId, platformUserName, avatarUrl, email);
	}
}
