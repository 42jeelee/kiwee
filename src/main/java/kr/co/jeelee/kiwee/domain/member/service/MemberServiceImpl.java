package kr.co.jeelee.kiwee.domain.member.service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.auth.oauth.dto.OAuth2UserInfo;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.authorization.entity.Role;
import kr.co.jeelee.kiwee.domain.authorization.model.RoleType;
import kr.co.jeelee.kiwee.domain.authorization.service.RoleService;
import kr.co.jeelee.kiwee.domain.member.dto.request.MemberRolesRequest;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberRolesResponse;
import kr.co.jeelee.kiwee.domain.member.exception.MemberNotHaveRoleException;
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
import kr.co.jeelee.kiwee.global.util.NicknameCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final NicknameCreator nicknameCreator;

	private final RoleService roleService;

	@Override
	@Transactional
	public MemberDetailResponse createMember(MemberCreateRequest memberCreateRequest) {
		String nickname = Optional.ofNullable(memberCreateRequest.nickname())
			.map(String::trim)
			.filter(n -> !n.isEmpty())
			.orElseGet(this::getNotDuplicateNickname);

		Member member = Member.of(
			memberCreateRequest.name(),
			nickname,
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
	public MemberRolesResponse getMemberRoles(UUID id) {
		return memberRepository.findById(id)
			.map(MemberRolesResponse::from)
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
	public MemberRolesResponse addRoles(UUID id, MemberRolesRequest request) {
		Member member = getById(id);

		Set<Role> roles = request.roles().stream()
				.map(roleService::findByName)
			.collect(Collectors.toSet());

		member.addRole(roles);
		return MemberRolesResponse.from(memberRepository.save(member));
	}

	@Override
	@Transactional
	public void deleteMemberById(UUID id) {
		memberRepository.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteRole(UUID id, String RoleName) {
		Member member = getById(id);
		Role role = roleService.findByName(RoleName);

		if (!member.getRoles().contains(role)) {
			throw new MemberNotHaveRoleException();
		}

		member.removeRole(role);
	}

	@Override
	@Transactional
	public Member createOrUpdateByOAuth(OAuth2UserInfo oAuth2UserInfo) {

		if (oAuth2UserInfo.email() != null) {
			Optional<Member> existedEmailMember =
				memberRepository.getWithRolesAndPermissionsByEmail(oAuth2UserInfo.email());

			if (existedEmailMember.isPresent()) {
				Member member = existedEmailMember.get();

				try {
					if (member.getAvatarUrl() == null && oAuth2UserInfo.avatarUrl() != null) {
						updateAvatarUrlIfChanged(member, oAuth2UserInfo.avatarUrl());
					}
				} catch (Exception e) {
					log.warn("Update member avatar url failed: {}", e.getMessage());
				}
				return member;
			}
		}

		Member member = Member.of(
			oAuth2UserInfo.name(),
			getNotDuplicateNickname(),
			oAuth2UserInfo.email(),
			oAuth2UserInfo.avatarUrl()
		);

		Role role = roleService.findByName(RoleType.MEMBER.name());
		member.addRole(Set.of(role));

		return memberRepository.save(member);
	}

	@Override
	public Member getById(UUID id) {
		return memberRepository.getWithRolesAndPermissionsById(id)
			.orElseThrow(MemberNotFoundException::new);
	}

	@Override
	public Authentication toAuthentication(UUID id) {
		Member member = getById(id);

		CustomOAuth2User oAuth2User = CustomOAuth2User.from(member, null);
		return new UsernamePasswordAuthenticationToken(oAuth2User, "", oAuth2User.getAuthorities());
	}

	private String getNotDuplicateNickname() {
		return IntStream.range(0, 10)
			.mapToObj(i -> nicknameCreator.createNickname())
			.filter(nickname -> !memberRepository.existsByNickname(nickname))
			.findFirst()
			.orElseThrow(
				() -> new FieldValidationException("nickname", "사용 가능한 닉네임을 찾지 못했습니다.\n다시 시도해주세요.")
			);
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
