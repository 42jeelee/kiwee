package kr.co.jeelee.kiwee.domain.member.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record MemberRolesRequest(
	@NotEmpty(message = "There must be at least one roles.") List<String> roles
) {
}
