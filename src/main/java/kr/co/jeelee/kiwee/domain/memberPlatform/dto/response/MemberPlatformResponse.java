package kr.co.jeelee.kiwee.domain.memberPlatform.dto.response;

import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.memberPlatform.entity.MemberPlatform;
import kr.co.jeelee.kiwee.domain.platform.dto.response.PlatformSimpleResponse;

public record MemberPlatformResponse(
	MemberSimpleResponse member, PlatformSimpleResponse platform,
	String userName, String avatarUrl, String email
) {
	public static MemberPlatformResponse from(MemberPlatform memberPlatform) {
		return new MemberPlatformResponse(
			MemberSimpleResponse.from(memberPlatform.getMember()),
			PlatformSimpleResponse.from(memberPlatform.getPlatform()),
			memberPlatform.getPlatformUserName(),
			memberPlatform.getAvatarUrl(),
			memberPlatform.getEmail()
		);
	}
}
