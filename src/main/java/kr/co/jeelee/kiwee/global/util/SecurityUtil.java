package kr.co.jeelee.kiwee.global.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import kr.co.jeelee.kiwee.domain.auth.exception.UnauthorizationException;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.global.model.PermissionType;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.global.exception.common.InvalidPrincipalException;

public class SecurityUtil {

	public static Member getLoginMember() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new UnauthorizationException();
		}

		Object principal = authentication.getPrincipal();
		if (principal instanceof CustomOAuth2User) {
			return ((CustomOAuth2User)principal).member();
		}

		throw new InvalidPrincipalException();
	}

	public static boolean hasAuthority(PermissionType permissionType) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new UnauthorizationException();
		}

		return authentication.getAuthorities().stream()
			.anyMatch(a -> a.getAuthority().equals(permissionType.name()));
	}

}
