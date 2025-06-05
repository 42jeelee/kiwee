package kr.co.jeelee.kiwee.domain.member.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import kr.co.jeelee.kiwee.domain.auth.oauth.dto.OAuth2UserInfo;
import kr.co.jeelee.kiwee.domain.member.dto.request.GainExpRequest;
import kr.co.jeelee.kiwee.domain.member.dto.request.MemberCreateRequest;
import kr.co.jeelee.kiwee.domain.member.dto.request.UpdateMemberRequest;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberDetailResponse;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;

public interface MemberService {

	MemberDetailResponse createMember(MemberCreateRequest memberCreateRequest);

	PagedResponse<MemberSimpleResponse> getAllMembers(Pageable pageable);
	PagedResponse<MemberSimpleResponse> searchMembers(String keyword, Pageable pageable);

	MemberDetailResponse getMemberById(UUID id);

	MemberDetailResponse updateMember(UUID id, UpdateMemberRequest updateMemberRequest);
	MemberDetailResponse gainExp(UUID id, GainExpRequest request);

	void deleteMemberById(UUID id);

	Member createByOAuth(OAuth2UserInfo oAuth2UserInfo);

	Member getById(UUID id);

	Authentication toAuthentication(UUID id);

}
