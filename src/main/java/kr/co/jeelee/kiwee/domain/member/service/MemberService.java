package kr.co.jeelee.kiwee.domain.member.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.member.dto.request.GainExpRequest;
import kr.co.jeelee.kiwee.domain.member.dto.request.MemberCreateRequest;
import kr.co.jeelee.kiwee.domain.member.dto.request.UpdateAvatarRequest;
import kr.co.jeelee.kiwee.domain.member.dto.request.UpdateEmailRequest;
import kr.co.jeelee.kiwee.domain.member.dto.request.UpdateNicknameRequest;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberDetailResponse;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;

public interface MemberService {

	MemberDetailResponse createMember(MemberCreateRequest memberCreateRequest);

	PagedResponse<MemberSimpleResponse> getAllMembers(Pageable pageable);
	PagedResponse<MemberSimpleResponse> searchMembers(String keyword, Pageable pageable);

	MemberDetailResponse getMemberById(UUID id);

	MemberDetailResponse changeNickname(UUID id, UpdateNicknameRequest request);
	MemberDetailResponse changeEmail(UUID id, UpdateEmailRequest request);
	MemberDetailResponse changeAvatarUrl(UUID id, UpdateAvatarRequest request);
	MemberDetailResponse gainExp(UUID id, GainExpRequest request);

	void deleteMemberById(UUID id);
}
