package kr.co.jeelee.kiwee.domain.channel.service;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.authorization.model.PermissionType;
import kr.co.jeelee.kiwee.domain.channel.dto.request.ChannelCreateRequest;
import kr.co.jeelee.kiwee.domain.channel.dto.request.ChannelUpdateRequest;
import kr.co.jeelee.kiwee.domain.channel.dto.response.ChannelDetailResponse;
import kr.co.jeelee.kiwee.domain.channel.dto.response.ChannelSimpleResponse;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.channel.exception.ChannelNotFoundException;
import kr.co.jeelee.kiwee.domain.channel.repository.ChannelRepository;
import kr.co.jeelee.kiwee.domain.channelMember.service.ChannelMemberService;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class ChannelServiceImpl implements ChannelService {

	private final ChannelRepository channelRepository;

	private final ChannelMemberService  channelMemberService;

	@Override
	@Transactional
	public ChannelDetailResponse createChannel(CustomOAuth2User principal, ChannelCreateRequest request) {

		if (channelRepository.existsByName(request.name())) {
			throw new FieldValidationException("name", "이름이 중복되었습니다.");
		}

		Channel channel = channelRepository.save(
			Channel.of(
				request.name(),
				request.icon(),
				request.banner(),
				request.description(),
				request.isOriginal(),
				request.isPublic()
			)
		);

		channelMemberService.invitedChannel(principal.member(), channel);
		return ChannelDetailResponse.from(channel);
	}

	@Override
	public PagedResponse<ChannelSimpleResponse> getAllChannels(Pageable pageable) {
		return PagedResponse.of(channelRepository.findAll(pageable), ChannelSimpleResponse::from);
	}

	@Override
	public PagedResponse<ChannelSimpleResponse> getAllPublicChannels(CustomOAuth2User principal, Pageable pageable) {
		return PagedResponse.of(
			channelRepository.findPublicAndJoinChannels(principal.member().getId(), pageable),
			Function.identity()
		);
	}

	@Override
	public ChannelDetailResponse getChannelDetailById(CustomOAuth2User principal, UUID id) {
		Channel channel = channelRepository.findById(id)
			.orElseThrow(ChannelNotFoundException::new);

		validateMember(channel, principal.member(), Set.of());

		return ChannelDetailResponse.from(channel);
	}

	@Override
	@Transactional
	public ChannelDetailResponse updateChannel(CustomOAuth2User principal, UUID id, ChannelUpdateRequest request) {
		Channel channel = channelRepository.findById(id)
			.orElseThrow(ChannelNotFoundException::new);

		validateMember(channel, principal.member(), Set.of(PermissionType.ROLE_EDIT_CHANNEL));

		updateNameIfChanged(channel, request.name());
		updateIconIfChanged(channel, request.icon());
		updateBannerIfChanged(channel, request.banner());
		updateDescriptionIfChanged(channel, request.description());
		updateIsPublicIfChanged(channel, request.isPubic());

		return ChannelDetailResponse.from(channel);
	}

	@Override
	@Transactional
	public void deleteChannel(CustomOAuth2User principal, UUID id) {
		Channel channel = channelRepository.findById(id)
			.orElseThrow(ChannelNotFoundException::new);

		validateMember(channel, principal.member(), Set.of(PermissionType.ROLE_DELETE_CHANNEL));

		channelRepository.deleteById(id);
	}

	@Override
	public Channel getById(UUID id) {
		return channelRepository.findById(id)
			.orElseThrow(ChannelNotFoundException::new);
	}

	private void validateMember(Channel channel, Member member, Set<PermissionType> permissionTypes) {
		if (!channelMemberService.isJoined(channel, member)) {
			throw new AccessDeniedException("가입되지 않은 채널입니다.");
		}

		if (!channelMemberService.isBen(channel, member)) {
			throw new AccessDeniedException("현재 해당 채널에서 벤 상태입니다.");
		}

		boolean hasPermission = permissionTypes.stream()
				.anyMatch(permissionType -> channelMemberService.hasPermission(channel, member, permissionType));

		if (!permissionTypes.isEmpty() && !hasPermission) {
			throw new AccessDeniedException("해당 채널 권한이 없습니다.");
		}
	}

	private void updateNameIfChanged(Channel channel, String name) {
		if (name != null && !name.equals(channel.getName())) {
			channel.updateName(name);
		}
	}

	private void updateIconIfChanged(Channel channel, String icon) {
		if (icon != null && !icon.equals(channel.getIcon())) {
			channel.updateIcon(icon);
		}
	}

	private void updateBannerIfChanged(Channel channel, String banner) {
		if (banner != null && !banner.equals(channel.getBanner())) {
			channel.updateBanner(banner);
		}
	}

	private void updateDescriptionIfChanged(Channel channel, String description) {
		if (description != null && !description.equals(channel.getDescription())) {
			channel.updateDescription(description);
		}
	}

	private void updateIsPublicIfChanged(Channel channel, Boolean isPublic) {
		if (isPublic != null && !isPublic.equals(channel.getIsPublic())) {
			if (isPublic) channel.isPublic();
			else channel.isPrivate();
		}
	}
}
