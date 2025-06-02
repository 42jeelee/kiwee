package kr.co.jeelee.kiwee.domain.reputations.dto.request;

import java.time.YearMonth;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReputationsStatsCreateRequest(
	@NotNull(message = "memberId can't be Null.") UUID memberId,
	@NotBlank(message = "yearMonth can't be Blank.") YearMonth yearMonth
) {
}
