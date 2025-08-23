package kr.co.jeelee.kiwee.domain.memberTheme.service;

import java.util.UUID;

public interface MemberThemeService {

	String updateOrCreateMemberTheme(UUID memberId, String name);

	String getMemberTheme(UUID memberId);

}
