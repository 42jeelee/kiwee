package kr.co.jeelee.kiwee.domain.member.dto.request;

import jakarta.validation.constraints.Min;

public record GainExpRequest(
	@Min(value = 1, message = "Exp must be greater than or equal to 1.")
	long exp
) {}
