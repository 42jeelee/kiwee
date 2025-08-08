package kr.co.jeelee.kiwee.domain.memberPlatform.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.memberPlatform.dto.request.MemberPlatformTokenRequest;
import kr.co.jeelee.kiwee.domain.auth.oauth.dto.OAuth2UserInfo;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.memberPlatform.dto.response.MemberPlatformResponse;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface MemberPlatformService {

	PagedResponse<MemberPlatformResponse> getMemberPlatforms(UUID memberId, UUID platformId, Pageable pageable);

	Member getOrCreateMemberByOAuth(OAuth2UserInfo oAuth2UserInfo);

	void toggleToken(long id, MemberPlatformTokenRequest request);

	void deleteMemberPlatform(long id);

}
