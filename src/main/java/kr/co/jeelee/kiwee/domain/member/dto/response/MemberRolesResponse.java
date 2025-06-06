package kr.co.jeelee.kiwee.domain.member.dto.response;

import java.util.List;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.authorization.dto.response.RoleResponse;
import kr.co.jeelee.kiwee.domain.member.entity.Member;

public record MemberRolesResponse(
	UUID id, String name, List<RoleResponse> roles
) {
	public static MemberRolesResponse from(Member member) {
		return new MemberRolesResponse(
			member.getId(),
			member.getName(),
			member.getRoles().stream()
				.map(RoleResponse::from)
				.toList()
		);
	}
}
