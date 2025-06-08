package kr.co.jeelee.kiwee.domain.channelMember.service;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.authorization.dto.request.RolesRequest;
import kr.co.jeelee.kiwee.domain.authorization.entity.Role;
import kr.co.jeelee.kiwee.domain.authorization.model.PermissionType;
import kr.co.jeelee.kiwee.domain.authorization.model.RoleType;
import kr.co.jeelee.kiwee.domain.authorization.service.RoleService;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.channel.exception.ChannelNotFoundException;
import kr.co.jeelee.kiwee.domain.channel.repository.ChannelRepository;
import kr.co.jeelee.kiwee.domain.channelMember.dto.response.ChannelMemberResponse;
import kr.co.jeelee.kiwee.domain.channelMember.entity.ChannelMember;
import kr.co.jeelee.kiwee.domain.channelMember.entity.ChannelMemberRole;
import kr.co.jeelee.kiwee.domain.channelMember.repository.ChannelMemberRepository;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.exception.common.InvalidParameterException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelMemberServiceImpl implements ChannelMemberService {

	private final ChannelMemberRepository channelMemberRepository;
	private final ChannelRepository channelRepository;

	private final MemberService memberService;
	private final RoleService roleService;

	@Override
	@Transactional
	public ChannelMemberResponse joinPublicChannel(CustomOAuth2User principal, UUID channelId) {
		Channel channel = channelRepository.findById(channelId)
			.orElseThrow(ChannelNotFoundException::new);

		if (!channel.getIsPublic()) {
			throw new AccessDeniedException("해당 채널은 비공개 채널입니다.");
		}

		Member member = memberService.getById(principal.member().getId());

		if (isJoined(channel, member)) {
			throw new FieldValidationException("member", "이미 가입되어 있습니다.");
		}

		ChannelMember cm = ChannelMember.of(channel, member);

		Role defaultRole = roleService.findByRoleType(RoleType.CHANNEL_MEMBER);
		cm.addRole(ChannelMemberRole.of(cm, defaultRole));

		return ChannelMemberResponse.from(channelMemberRepository.save(cm));
	}

	@Override
	@Transactional
	public ChannelMemberResponse addRolesAtChannel(
		CustomOAuth2User principal, UUID channelId, UUID memberId, RolesRequest request
	) {
		ChannelMember cm = channelMemberRepository.getWithChannelAndMemberByChannelIdAndMemberId(
			channelId, memberId
		).orElseThrow(() -> new InvalidParameterException("가입 기록이 없습니다."));

		validatePermission(
			principal.member(), cm.getChannel(), cm.getMember(), PermissionType.ROLE_CHANNEL_PERMISSION_GRANTER
		);

		cm.addRoles(request.roles().stream()
			.map(r -> ChannelMemberRole.of(cm, roleService.findByRoleType(r)))
			.collect(Collectors.toSet())
		);

		return ChannelMemberResponse.from(channelMemberRepository.save(cm));
	}

	@Override
	@Transactional
	public ChannelMemberResponse removeRoleAtChannel(CustomOAuth2User principal, UUID channelId, UUID memberId, RoleType roleType) {
		ChannelMember cm = channelMemberRepository.getWithChannelAndMemberByChannelIdAndMemberId(
			channelId, memberId
		).orElseThrow(() -> new InvalidParameterException("가입 기록이 없습니다."));

		validatePermission(
			principal.member(), cm.getChannel(), cm.getMember(), PermissionType.ROLE_CHANNEL_PERMISSION_GRANTER
		);

		ChannelMemberRole cmr = cm.getRole(roleType);

		if (cmr == null) {
			throw new FieldValidationException("roleType", "해당 역할이 부여되지 않았습니다.");
		}

		cm.removeRole(cmr);
		return ChannelMemberResponse.from(channelMemberRepository.save(cm));
	}

	@Override
	public PagedResponse<ChannelMemberResponse> findMembersByChannel(
		CustomOAuth2User principal, UUID channelId, Pageable pageable
	) {
		ChannelMember cm = channelMemberRepository.getWithChannelAndMemberByChannelIdAndMemberId(
			channelId, principal.member().getId()
		).orElseThrow(() -> new AccessDeniedException("해당 채널에 가입되어 있지 않습니다."));

		return PagedResponse.of(
			channelMemberRepository.getChannelMembersByChannel(cm.getChannel(), pageable),
			ChannelMemberResponse::from
		);
	}

	@Override
	@Transactional
	public ChannelMemberResponse updateBen(
		CustomOAuth2User principal, UUID channelId, UUID memberId, boolean isBen
	) {
		ChannelMember cm = channelMemberRepository.getWithChannelAndMemberByChannelIdAndMemberId(
			channelId, memberId
		).orElseThrow(() -> new InvalidParameterException("가입 기록이 없습니다."));

		validatePermission(
			principal.member(), cm.getChannel(), cm.getMember(), PermissionType.ROLE_CHANNEL_BEN_GRANTER
		);

		if (isBen) cm.ben();
		else cm.unBen();

		return ChannelMemberResponse.from(channelMemberRepository.save(cm));
	}

	@Override
	@Transactional
	public void kickChannelMember(CustomOAuth2User principal, UUID channelId, UUID memberId) {
		ChannelMember cm = channelMemberRepository.getWithChannelAndMemberByChannelIdAndMemberId(
			channelId, memberId
		).orElseThrow(() -> new InvalidParameterException("가입 기록이 없습니다."));

		validatePermission(
			principal.member(), cm.getChannel(), cm.getMember(), PermissionType.ROLE_CHANNEL_KICK_MEMBER
		);

		channelMemberRepository.delete(cm);
	}

	@Override
	public boolean isBen(Channel channel, Member member) {
		return channelMemberRepository.isBenByChannelAndMember(channel, member);
	}

	@Override
	public boolean isJoined(Channel channel, Member member) {
		return channelMemberRepository.existsByChannelAndMember(channel, member);
	}

	@Override
	public boolean hasRole(Channel channel, Member member, RoleType roleType) {
		return channelMemberRepository.hasRoleType(channel, member, roleType);
	}

	@Override
	public boolean hasPermission(Channel channel, Member member, PermissionType permissionType) {
		return channelMemberRepository.hasPermissionType(channel.getId(), member.getId(), permissionType);
	}

	@Override
	public void invitedChannel(Member member, Channel channel) {
		if (isJoined(channel, member)) {
			throw new FieldValidationException("member", "이미 가입되어 있습니다.");
		}

		ChannelMember cm = ChannelMember.of(channel, member);

		Role defaultRole = roleService.findByRoleType(RoleType.CHANNEL_MEMBER);
		cm.addRole(ChannelMemberRole.of(cm, defaultRole));

		channelMemberRepository.save(cm);
	}

	private void validatePermission(
		Member loginedMember, Channel channel, Member member, PermissionType permissionType
	) {
		if (!hasPermission(channel, loginedMember, permissionType)) {
			throw new AccessDeniedException("해당 권한이 없습니다.");
		}

		if (hasRole(channel, member, RoleType.CHANNEL_MANAGER)) {
			throw new AccessDeniedException("대상이 해당 체널 메니저 권한이 있습니다.");
		}
	}

}
