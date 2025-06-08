package kr.co.jeelee.kiwee.domain.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.auth.dto.request.MemberPlatformTokenRequest;
import kr.co.jeelee.kiwee.domain.auth.entity.MemberPlatform;
import kr.co.jeelee.kiwee.domain.auth.exception.MemberPlatformNotFoundException;
import kr.co.jeelee.kiwee.domain.auth.oauth.dto.OAuth2UserInfo;
import kr.co.jeelee.kiwee.domain.auth.repository.MemberPlatformRepository;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;
import kr.co.jeelee.kiwee.domain.platform.service.PlatformService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Getter
@Transactional(readOnly = true)
public class MemberPlatformServiceImpl implements MemberPlatformService {

	private final MemberPlatformRepository memberPlatformRepository;

	private final MemberService memberService;
	private final PlatformService platformService;

	@Override
	@Transactional
	public Member getOrCreateMemberByOAuth(OAuth2UserInfo oAuth2UserInfo) {
		Platform platform = platformService.getEntityByProvider(oAuth2UserInfo.provider());
		Optional<MemberPlatform> existingMemberPlatform =
			memberPlatformRepository.getWithAll(platform, oAuth2UserInfo.id());

		if (existingMemberPlatform.isPresent()) {
			MemberPlatform mp = existingMemberPlatform.get();

			mp.changeProvideData(
				oAuth2UserInfo.name(),
				oAuth2UserInfo.avatarUrl(),
				oAuth2UserInfo.email()
			);
			return mp.getMember();
		}

		Member member = memberService.createOrUpdateByOAuth(oAuth2UserInfo);

		MemberPlatform mp = MemberPlatform.of(
			member, platform, oAuth2UserInfo.id(), oAuth2UserInfo.name(), oAuth2UserInfo.avatarUrl(),
			oAuth2UserInfo.email(), false, null, null, null
		);

		memberPlatformRepository.save(mp);
		return member;
	}

	@Override
	@Transactional
	public void toggleToken(long id, MemberPlatformTokenRequest request) {
		MemberPlatform memberPlatform = memberPlatformRepository.findById(id)
			.orElseThrow(MemberPlatformNotFoundException::new);

		memberPlatform.toggleToken(
			request.isToken(),
			request.accessToken(),
			request.refreshToken(),
			request.tokenExpireAt()
		);
	}

	@Override
	@Transactional
	public void deleteMemberPlatform(long id) {
		memberPlatformRepository.deleteById(id);
	}
}
