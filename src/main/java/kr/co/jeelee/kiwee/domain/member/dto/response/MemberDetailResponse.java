package kr.co.jeelee.kiwee.domain.member.dto.response;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.member.entity.Member;

public record MemberDetailResponse(
	UUID id, String name, String nickname, String email, String avatarUrl,
	int level, long exp, boolean isActive
) {
	public static MemberDetailResponse from(Member member) {
		return new MemberDetailResponse(
			member.getId(),
			member.getName(),
			member.getNickname(),
			member.getEmail(),
			member.getAvatarUrl(),
			member.getLevel(),
			member.getExp(),
			member.isActive()
		);
	}
}
