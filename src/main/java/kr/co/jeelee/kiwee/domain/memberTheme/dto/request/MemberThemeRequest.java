package kr.co.jeelee.kiwee.domain.memberTheme.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberThemeRequest(
	@NotBlank(message = "name can't be Blank.") String name
) {
}
