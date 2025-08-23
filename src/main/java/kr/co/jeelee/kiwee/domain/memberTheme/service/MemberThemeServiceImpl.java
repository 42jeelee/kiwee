package kr.co.jeelee.kiwee.domain.memberTheme.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.memberTheme.entity.MemberTheme;
import kr.co.jeelee.kiwee.domain.memberTheme.exception.MemberThemeNotFoundException;
import kr.co.jeelee.kiwee.domain.memberTheme.repository.MemberThemeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberThemeServiceImpl implements MemberThemeService{

	private final MemberThemeRepository memberThemeRepository;

	private final MemberService memberService;

	@Override
	@Transactional
	public String updateOrCreateMemberTheme(UUID memberId, String name) {
		MemberTheme theme = memberThemeRepository.findByMemberId(memberId)
			.orElseGet(() -> {
				Member member = memberService.getById(memberId);
				return MemberTheme.of(member, name);
			});

		theme.changeName(name);
		memberThemeRepository.save(theme);
		return theme.getName();
	}

	@Override
	public String getMemberTheme(UUID memberId) {
		return memberThemeRepository.findByMemberId(memberId)
			.map(MemberTheme::getName)
			.orElseThrow(MemberThemeNotFoundException::new);
	}

}
