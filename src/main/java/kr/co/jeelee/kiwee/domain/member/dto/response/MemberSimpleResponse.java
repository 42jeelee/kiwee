package kr.co.jeelee.kiwee.domain.member.dto.response;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.member.entity.Member;

public record MemberSimpleResponse(
	UUID id, String nickname, String email,
	String avatarUrl, int level, long exp
) {
	public static MemberSimpleResponse from(Member member) {
		return new MemberSimpleResponse(
			member.getId(),
			member.getNickname(),
			member.getEmail(),
			member.getAvatarUrl(),
			member.getLevel(),
			member.getExp()
		);
	}
}
