package kr.co.jeelee.kiwee.domain.channel.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.channel.dto.request.ChannelCreateRequest;
import kr.co.jeelee.kiwee.domain.channel.dto.request.ChannelUpdateRequest;
import kr.co.jeelee.kiwee.domain.channel.dto.response.ChannelDetailResponse;
import kr.co.jeelee.kiwee.domain.channel.dto.response.ChannelSimpleResponse;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface ChannelService {

	ChannelDetailResponse createChannel(CustomOAuth2User principal, ChannelCreateRequest request);

	PagedResponse<ChannelSimpleResponse> getAllChannels(Pageable pageable);
	PagedResponse<ChannelSimpleResponse> getAllPublicChannels(CustomOAuth2User principal, Pageable pageable);

	ChannelDetailResponse getChannelDetailById(CustomOAuth2User principal, UUID id);

	ChannelDetailResponse updateChannel(CustomOAuth2User principal, UUID id, ChannelUpdateRequest request);

	void deleteChannel(CustomOAuth2User principal, UUID id);

	Channel getById(UUID id);

}
