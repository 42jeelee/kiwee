package kr.co.jeelee.kiwee.domain.invite.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.invite.dto.request.InviteCreateRequest;
import kr.co.jeelee.kiwee.domain.invite.dto.response.InviteDetailResponse;
import kr.co.jeelee.kiwee.domain.invite.dto.response.InviteSimpleResponse;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;

public interface InviteService {

	InviteDetailResponse invite(CustomOAuth2User principal, InviteCreateRequest request);

	InviteDetailResponse getById(CustomOAuth2User principal, UUID id);
	InviteDetailResponse getByCode(CustomOAuth2User principal, String code);

	PagedResponse<InviteSimpleResponse> getAllPublicInvite(Pageable pageable);
	PagedResponse<InviteSimpleResponse> getReceiveMe(CustomOAuth2User principal, Pageable pageable);

	void accept(CustomOAuth2User principal, String code);
	void reject(CustomOAuth2User principal, String code);
	void expired(String code);

	void expiredMyInvite(CustomOAuth2User principal, String code);

	boolean validateCode(String code);

}
