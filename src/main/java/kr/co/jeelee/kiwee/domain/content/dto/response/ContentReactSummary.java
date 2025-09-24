package kr.co.jeelee.kiwee.domain.content.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ContentReactSummary(
	UUID id,
	Long recN2Count, Long recN1Count, Long rec0Count, Long recP1Count, Long recP2Count,
	Double star, Long starCount,
	LocalDateTime lastUpdatedAt
) {
}
