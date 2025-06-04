package kr.co.jeelee.kiwee.domain.auth.service;

import kr.co.jeelee.kiwee.domain.auth.dto.request.MemberPlatformTokenRequest;
import kr.co.jeelee.kiwee.domain.auth.oauth.dto.OAuth2UserInfo;
import kr.co.jeelee.kiwee.domain.member.entity.Member;

public interface MemberPlatformService {

	Member getOrCreateMemberByOAuth(OAuth2UserInfo oAuth2UserInfo);

	void toggleToken(long id, MemberPlatformTokenRequest request);

	void deleteMemberPlatform(long id);

}
