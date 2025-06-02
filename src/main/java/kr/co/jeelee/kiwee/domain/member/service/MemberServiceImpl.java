package kr.co.jeelee.kiwee.domain.member.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.member.repository.MemberRepository;
import kr.co.jeelee.kiwee.domain.member.dto.request.GainExpRequest;
import kr.co.jeelee.kiwee.domain.member.dto.request.MemberCreateRequest;
import kr.co.jeelee.kiwee.domain.member.dto.request.UpdateMemberRequest;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberDetailResponse;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.exception.MemberNotFoundException;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public MemberDetailResponse createMember(MemberCreateRequest memberCreateRequest) {
		Member member = Member.of(
			memberCreateRequest.name(),
			memberCreateRequest.nickname(),
			memberCreateRequest.email(),
			memberCreateRequest.avatarUrl()
		);

		if (memberRepository.existsByNickname(member.getNickname())) {
			throw new FieldValidationException("nickname", "닉네임이 중복되었습니다.");
		}

		if (member.getEmail() != null && memberRepository.existsByEmail(member.getEmail())) {
			throw new FieldValidationException("email", "이메일이 중복되었습니다.");
		}

		return MemberDetailResponse.from(memberRepository.save(member));
	}

	@Override
	public PagedResponse<MemberSimpleResponse> getAllMembers(Pageable pageable) {
		return PagedResponse.of(memberRepository.findAll(pageable), MemberSimpleResponse::from);
	}

	@Override
	public PagedResponse<MemberSimpleResponse> searchMembers(String keyword, Pageable pageable) {
		return PagedResponse.of(
			memberRepository.findByNicknameContaining(keyword, pageable),
			MemberSimpleResponse::from
		);
	}

	@Override
	public MemberDetailResponse getMemberById(UUID id) {
		return memberRepository.findById(id)
			.map(MemberDetailResponse::from)
			.orElseThrow(MemberNotFoundException::new);
	}

	@Override
	@Transactional
	public MemberDetailResponse updateMember(UUID id, UpdateMemberRequest request) {
		Member member = memberRepository.findById(id)
			.orElseThrow(MemberNotFoundException::new);

		updateNameIfChanged(member, request.name());
		updateNicknameIfChanged(member, request.nickname());
		updateEmailIfChanged(member, request.email());
		updateAvatarUrlIfChanged(member, request.avatarUrl());

		return MemberDetailResponse.from(member);
	}

	@Override
	@Transactional
	public MemberDetailResponse gainExp(UUID id, GainExpRequest request) {
		Member member = memberRepository.findById(id)
			.orElseThrow(MemberNotFoundException::new);

		member.gainExp(request.exp());
		return MemberDetailResponse.from(memberRepository.save(member));
	}

	@Override
	@Transactional
	public void deleteMemberById(UUID id) {
		memberRepository.deleteById(id);
	}

	private void updateNameIfChanged(Member member, String name) {
		if (name != null && !name.equals(member.getNickname())) {
			member.updateName(name);
		}
	}

	private void updateNicknameIfChanged(Member member, String nickname) {
		if (nickname != null && !nickname.equals(member.getNickname())) {
			if (memberRepository.existsByNickname(nickname)) {
				throw new FieldValidationException("nickname", "닉네임이 중복되었습니다.");
			}
			member.updateNickname(nickname);
		}
	}

	private void updateEmailIfChanged(Member member, String email) {
		if (email != null && !email.equals(member.getEmail())) {
			if (memberRepository.existsByEmail(email)) {
				throw new FieldValidationException("email", "이메일이 중복되었습니다.");
			}
			member.updateEmail(email);
		}
	}

	private void updateAvatarUrlIfChanged(Member member, String avatarUrl) {
		if (avatarUrl != null && !avatarUrl.equals(member.getAvatarUrl())) {
			member.updateAvatarUrl(avatarUrl);
		}
	}
}
