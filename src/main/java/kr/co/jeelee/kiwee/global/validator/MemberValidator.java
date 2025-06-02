package kr.co.jeelee.kiwee.global.validator;

import java.util.UUID;

import org.springframework.stereotype.Component;

import kr.co.jeelee.kiwee.domain.member.repository.MemberRepository;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberValidator {

	private final MemberRepository memberRepository;

	public Member getMe() {
		throw new UnsupportedOperationException();
	}

	public Member getMember(UUID id) {
		return memberRepository.findById(id)
			.orElseThrow(MemberNotFoundException::new);
	}

}
