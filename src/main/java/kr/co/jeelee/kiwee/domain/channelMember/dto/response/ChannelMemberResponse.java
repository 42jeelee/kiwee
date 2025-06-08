package kr.co.jeelee.kiwee.domain.channelMember.dto.response;

import java.util.List;

import kr.co.jeelee.kiwee.domain.authorization.dto.response.RoleResponse;
import kr.co.jeelee.kiwee.domain.channelMember.entity.ChannelMember;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;

public record ChannelMemberResponse(
	MemberSimpleResponse member,
	List<RoleResponse> roles,
	boolean isBen
) {
	public static ChannelMemberResponse from(ChannelMember channelMember) {
		return new ChannelMemberResponse(
			MemberSimpleResponse.from(channelMember.getMember()),
			channelMember.getRoles().stream()
				.map(cmr -> RoleResponse.from(cmr.getRole()))
				.toList(),
			channelMember.getIsBen()
		);
	}
}
