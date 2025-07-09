package kr.co.jeelee.kiwee.domain.invite.service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.authorization.model.PermissionType;
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
import kr.co.jeelee.kiwee.domain.memberActivity.event.MemberActivityEvent;
import kr.co.jeelee.kiwee.domain.memberActivity.model.ActivityType;
import kr.co.jeelee.kiwee.domain.notification.event.NotificationEvent;
import kr.co.jeelee.kiwee.domain.notification.model.NotificationType;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.exception.common.DomainNotFoundException;
import kr.co.jeelee.kiwee.global.exception.common.InvalidParameterException;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import kr.co.jeelee.kiwee.global.resolver.DomainResponseResolver;
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

	private final ApplicationEventPublisher eventPublisher;

	private final DomainObjectResolver domainObjectResolver;

	@Override
	@Transactional
	public InviteDetailResponse invite(CustomOAuth2User principal, InviteCreateRequest request) {
		Object target = domainObjectResolver.resolve(request.domain(), request.targetId());

		if (target instanceof Channel) {
			if (
				!channelMemberService.hasPermission(
					(Channel) target,
					principal.member(),
					PermissionType.ROLE_CHANNEL_INVITE_MEMBER
				)
			) {
				throw new AccessDeniedException("해당 채널 권한이 없습니다.");
			}
		}

		Object targetResponse = DomainResponseResolver.toResponse(target);

		Member invitee = request.inviteeId() != null
			? memberService.getById(request.inviteeId())
			: null;

		if (principal.member().equals(invitee)) {
			throw new InvalidParameterException("invitee", "자신을 초대할 수는 없습니다.");
		}

		String code = Base62Encoder.encode(UUID.randomUUID());

		Invite invite = Invite.of(
			request.domain(), request.targetId(), principal.member(), invitee,
			code, InviteStatus.PENDING, request.message(), request.condition(),
			request.maxUses(), 0
		);
		Invite savedInvite = inviteRepository.save(invite);

		eventPublisher.publishEvent(MemberActivityEvent.of(
			principal.member().getId(),
			request.domain(),
			request.targetId(),
			ActivityType.INVITE
		));

		if (invitee != null) {
			eventPublisher.publishEvent(getNotificationEvent(invite, target));
		}

		return InviteDetailResponse.from(savedInvite, targetResponse);
	}

	@Override
	public InviteDetailResponse getById(CustomOAuth2User principal, UUID id) {
		Invite invite = inviteRepository.findById(id)
			.orElseThrow(InviteNotFoundException::new);

		return inviteDetailResponse(principal.member(), invite);
	}

	@Override
	public InviteDetailResponse getByCode(CustomOAuth2User principal, String code) {
		Invite invite = inviteRepository.findByCode(code)
			.orElseThrow(InviteNotFoundException::new);

		return inviteDetailResponse(principal.member(), invite);
	}

	@Override
	public PagedResponse<InviteSimpleResponse> getAllPublicInvite(Pageable pageable) {
		return PagedResponse.of(inviteRepository.findByInviteeNull(pageable), invite -> {
			Object target = domainObjectResolver.resolve(invite.getDomain(), invite.getTargetId());

			return InviteSimpleResponse.from(invite, DomainResponseResolver.toResponse(target));
		});
	}

	@Override
	public PagedResponse<InviteSimpleResponse> getReceiveMe(CustomOAuth2User principal, Pageable pageable) {
		return PagedResponse.of(
			inviteRepository.findByInviteeId(principal.member().getId(), pageable),
			invite -> {
				Object target = domainObjectResolver.resolve(invite.getDomain(), invite.getTargetId());

				return InviteSimpleResponse.from(invite, DomainResponseResolver.toResponse(target));
			}
		);
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
	@Transactional
	public void expiredMyInvite(CustomOAuth2User principal, String code) {
		Invite invite = inviteRepository.findByCode(code)
			.orElseThrow(InviteNotFoundException::new);

		if (!invite.getInviter().equals(principal.member())) {
			throw new AccessDeniedException("초대권 발행인이 아닙니다.");
		}

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

	private InviteDetailResponse inviteDetailResponse(Member member, Invite invite) {
		if (invite.getInvitee() != null && !invite.getInvitee().equals(member)) {
			throw new AccessDeniedException("초대 받은 맴버가 아닙니다.");
		}

		Object target = domainObjectResolver.resolve(invite.getDomain(), invite.getTargetId());
		Object targetResponse = DomainResponseResolver.toResponse(target);

		return InviteDetailResponse.from(invite, targetResponse);
	}

	private void joinObject(Member member, Invite invite) {
		switch (invite.getDomain()) {
			case CHANNEL:
				Channel channel = channelService.getById(invite.getTargetId());

				invite.accept();
				channelMemberService.invitedChannel(member, channel, Set.of());
				inviteRepository.save(invite);
				break;
		}
	}

	private NotificationEvent getNotificationEvent(Invite invite, Object target) {
		String title;
		String message;

		switch (invite.getDomain()) {
			case CHANNEL -> {
				Channel channel = (Channel) target;
				title = String.format("채널 '%s'에 초대 받았어요!", channel.getName());
				message = String.format("%s님이 '%s' 채널에 당신을 초대하였습니다:)", invite.getInviter().getNickname(), channel.getName());
			}
			default -> throw new DomainNotFoundException();
		}

		return new NotificationEvent(
			invite.getInvitee().getId(),
			NotificationType.INVITE,
			title,
			message,
			invite.getDomain(),
			invite.getTargetId()
		);
	}
}
