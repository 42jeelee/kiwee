package kr.co.jeelee.kiwee.domain.channelMember.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.authorization.dto.request.RolesRequest;
import kr.co.jeelee.kiwee.domain.authorization.model.PermissionType;
import kr.co.jeelee.kiwee.domain.authorization.model.RoleType;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.channelMember.dto.response.ChannelMemberResponse;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;

public interface ChannelMemberService {

	ChannelMemberResponse joinPublicChannel(CustomOAuth2User principal, UUID channelId);

	ChannelMemberResponse addRolesAtChannel(CustomOAuth2User principal, UUID channelId, UUID memberId, RolesRequest request);
	ChannelMemberResponse removeRoleAtChannel(CustomOAuth2User principal, UUID channelId, UUID memberId, RoleType roleType);

	PagedResponse<ChannelMemberResponse> findMembersByChannel(CustomOAuth2User principal, UUID channelId, Pageable pageable);

	ChannelMemberResponse updateBen(CustomOAuth2User principal, UUID channelId, UUID memberId, boolean isBen);

	void kickChannelMember(CustomOAuth2User principal, UUID channelId, UUID memberId);

	boolean isBen(Channel channel, Member member);
	boolean isJoined(Channel channel, Member member);
	boolean hasRole(Channel channel, Member member, RoleType roleType);
	boolean hasPermission(Channel channel, Member member, PermissionType permissionType);

	void invitedChannel(Member member, Channel channel);

}
