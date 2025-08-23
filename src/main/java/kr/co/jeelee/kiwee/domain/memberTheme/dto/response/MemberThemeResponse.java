package kr.co.jeelee.kiwee.domain.memberTheme.dto.response;

public record MemberThemeResponse(String name) {
	public static MemberThemeResponse from(String name) {
		return new MemberThemeResponse(name);
	}
}
