package kr.co.jeelee.kiwee.domain.contentMember.dto.request;

import java.time.LocalDateTime;

public record ContentMemberUpdateRequest(
	Boolean isCompleted, Integer recommended, String recommendReason,
	LocalDateTime startAt, Double star, Long consumedAmount
) {
}
