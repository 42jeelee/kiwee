package kr.co.jeelee.kiwee.global.model;

import kr.co.jeelee.kiwee.domain.platform.entity.Platform;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlatformInfo {
	GOOGLE(
		"구글",
		"https://developers.google.com/identity/images/g-logo.png",
		"/googleLogo.png",
		"구글(Google LLC)는 1998년 미국 캘리포니아주 멘로파크에서 래리 페이지(Larry Page)와 세르게이 브린(Sergey Brin)이 설립한 다국적 기술 기업입니다. 현재는 알파벳(Alphabet Inc.)의 자회사로서, 주로 인터넷 검색, 온라인 광고, 클라우드 컴퓨팅, 소프트웨어, 인공지능(AI) 분야에 집중하고 있습니다.",
		"https://www.google.com",
		"google",
		false
	),
	DISCORD(
		"디스코드",
		"/discordLogo.svg",
		"https://mblogthumb-phinf.pstatic.net/MjAyMDEwMDJfMjA3/MDAxNjAxNjIzMjE4NTkw.kAQZ3VohVtNecMcD5HIj6A32AjsyEdX8HeHOwfn3Lmsg.us7LNscMa6mlYpV8hr2ha00FTuW-VP8iVLYQgFMVEpYg.PNG.alscks140/dsdsad.png?type=w800",
		"사실상 현재는 게이밍 메신저의 대명사로, 채팅, 통화, 화면 공유 등을 지원하는 인스턴트 메신저. 2015년 5월 13일에 모바일 MOBA 게임이었던 Fates Forever를 지원하기 위하여 출시되었다. 주로 온라인 게임을 즐기는 게이머들이 많이 이용하는 메신저 프로그램, 뛰어난 성능과 간편함을 바탕으로 게이머들이 과거 애용하던 주류 메신저들을 뛰어넘고 현재 주요한 앱으로서 자리하게 되었다.",
		"https://discord.com/",
		"discord",
		false
	),
	TMDB(
		"TMDB",
		"https://files.readme.io/29c6fee-blue_short.svg",
		"https://www.themoviedb.org/assets/2/v4/logos/v2/blue_square_2-d537fb228cf3ded904ef09b136fe3fec72548ebc1fea3fbbd1ad9e36364db38b.svg",
		"영화, 텔레비전 관련 사이트. 정확히는 영화, TV 프로그램을 수집하여 데이터베이스로 만드는 회사이다.",
		"https://www.themoviedb.org/",
		"tmdb",
		false
	),
	;

	private final String name;
	private final String icon;
	private final String banner;
	private final String description;
	private final String homepage;
	private final String provider;
	private final boolean isToken;

	public Platform toEntity() {
		return Platform.of(name, icon, banner, description, homepage, provider, isToken);
	}
}
