package kr.co.jeelee.kiwee.domain.invite.service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.authorization.model.PermissionType;
import kr.co.jeelee.kiwee.domain.channel.dto.response.ChannelSimpleResponse;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.channel.service.ChannelService;
import kr.co.jeelee.kiwee.domain.channelMember.service.ChannelMemberService;
import kr.co.jeelee.kiwee.domain.invite.dto.InviteCondition;
import kr.co.jeelee.kiwee.domain.invite.dto.request.InviteCreateRequest;
import kr.co.jeelee.kiwee.domain.invite.dto.response.InviteDetailResponse;
import kr.co.jeelee.kiwee.domain.invite.dto.response.InviteSimpleResponse;
import kr.co.jeelee.kiwee.domain.invite.entity.Invite;
import kr.co.jeelee.kiwee.domain.invite.exception.InviteNotFoundException;
import kr.co.jeelee.kiwee.domain.invite.model.InviteStatus;
import kr.co.jeelee.kiwee.domain.invite.repository.InviteRepository;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.exception.common.DomainNotFoundException;
import kr.co.jeelee.kiwee.global.util.Base62Encoder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InviteServiceImpl implements InviteService {

	private final InviteRepository inviteRepository;

	private final MemberService memberService;
	private final ChannelService channelService;
	private final ChannelMemberService channelMemberService;

	@Override
	@Transactional
	public InviteDetailResponse invite(CustomOAuth2User principal, InviteCreateRequest request) {
		Object targetResponse = ValidateOrGetDomainObjectResponse(
			principal.member(), request.domain(), request.targetId(), Set.of(PermissionType.ROLE_CHANNEL_INVITE_MEMBER)
		);

		Member invitee = request.inviteeId() != null
			? memberService.getById(request.inviteeId())
			: null;

		String code = Base62Encoder.encode(UUID.randomUUID());

		Invite invite = Invite.of(
			request.domain(), request.targetId(), principal.member(), invitee,
			code, InviteStatus.PENDING, request.message(), request.condition(),
			request.maxUses(), 0
		);

		return InviteDetailResponse.from(inviteRepository.save(invite), targetResponse);
	}

	@Override
	public InviteDetailResponse getById(CustomOAuth2User principal, UUID id) {
		Invite invite = inviteRepository.findById(id)
			.orElseThrow(InviteNotFoundException::new);

		if (invite.getInvitee() != null && !invite.getInvitee().equals(principal.member())) {
			throw new AccessDeniedException("초대 받은 맴버가 아닙니다.");
		}

		Object targetResponse = ValidateOrGetDomainObjectResponse(
			principal.member(), invite.getDomain(), invite.getTargetId(), Set.of()
		);

		return InviteDetailResponse.from(invite, targetResponse);
	}

	@Override
	public InviteDetailResponse getByCode(CustomOAuth2User principal, String code) {
		Invite invite = inviteRepository.findByCode(code)
			.orElseThrow(InviteNotFoundException::new);

		if (invite.getInvitee() != null && !invite.getInvitee().equals(principal.member())) {
			throw new AccessDeniedException("초대 받은 맴버가 아닙니다.");
		}

		Object targetResponse = ValidateOrGetDomainObjectResponse(
			principal.member(), invite.getDomain(), invite.getTargetId(), Set.of()
		);

		return InviteDetailResponse.from(invite, targetResponse);
	}

	@Override
	public PagedResponse<InviteSimpleResponse> getAllPublicInvite(Pageable pageable) {
		return PagedResponse.of(inviteRepository.findByInviteeNull(pageable), invite -> {
			Object targetResponse = ValidateOrGetDomainObjectResponse(
				null, invite.getDomain(), invite.getTargetId(), Set.of()
			);

			return InviteSimpleResponse.from(invite, targetResponse);
		});
	}

	@Override
	public PagedResponse<InviteSimpleResponse> getReceiveMe(CustomOAuth2User principal, Pageable pageable) {
		return PagedResponse.of(
			inviteRepository.findByInviteeId(principal.member().getId(), pageable),
			invite -> {
				Object targetResponse = ValidateOrGetDomainObjectResponse(
					principal.member(), invite.getDomain(), invite.getTargetId(), Set.of()
			);

			return InviteSimpleResponse.from(invite, targetResponse);
		});
	}

	@Override
	@Transactional
	public void accept(CustomOAuth2User principal, String code) {
		Invite invite = inviteRepository.findByCode(code)
			.orElseThrow(InviteNotFoundException::new);

		if (invite.getInvitee() != null && invite.getInvitee().equals(principal.member())) {
			joinObject(principal.member(), invite);
			return ;
		}

		InviteCondition condition = invite.getCondition();
		if (condition == null || !(
			(condition.isEmail() && principal.member().getEmail().isEmpty())
			&& (condition.minLevel() > principal.member().getLevel())
			)
		) {
			joinObject(principal.member(), invite);
			return ;
		}

		throw new AccessDeniedException("초대 조건을 만족하지 못하였습니다.");
	}

	@Override
	@Transactional
	public void reject(CustomOAuth2User principal, String code) {
		Invite invite = inviteRepository.findByCode(code)
			.orElseThrow(InviteNotFoundException::new);

		if (invite.getInvitee() != null && invite.getInvitee().equals(principal.member())) {
			invite.reject();
			inviteRepository.save(invite);
		}
		throw new AccessDeniedException("거부할 수 없는 초대장입니다.");
	}

	@Override
	@Transactional
	public void expired(String code) {
		Invite invite = inviteRepository.findByCode(code)
			.orElseThrow(InviteNotFoundException::new);

		invite.expire();
		inviteRepository.save(invite);
	}

	@Override
	public boolean validateCode(String code) {
		Invite invite = inviteRepository.findByCode(code)
			.orElseThrow(InviteNotFoundException::new);

		return invite.getStatus().equals(InviteStatus.PENDING)
			&& invite.getExpiredAt().isAfter(LocalDateTime.now());
	}

	private void joinObject(Member member, Invite invite) {
		switch (invite.getDomain()) {
			case CHANNEL:
				Channel channel = channelService.getById(invite.getTargetId());

				invite.accept();
				channelMemberService.invitedChannel(member, channel);
				inviteRepository.save(invite);
				break;
		}
	}

	private Object ValidateOrGetDomainObjectResponse(
		Member loggedMember, DomainType domainType, UUID targetId, Set<PermissionType> permissionTypes
	) {
		switch (domainType) {
			case CHANNEL:
				Channel channel = channelService.getById(targetId);

				boolean hasPermission = permissionTypes.stream()
					.anyMatch(permissionType ->
						channelMemberService.hasPermission(channel, loggedMember, permissionType)
					);

				if (!permissionTypes.isEmpty() && !hasPermission) {
					throw new AccessDeniedException("해당 채널 권한이 없습니다.");
				}

				return ChannelSimpleResponse.from(channel);
		}

		throw new DomainNotFoundException();
	}

}
