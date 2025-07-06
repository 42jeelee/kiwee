package kr.co.jeelee.kiwee.domain.questMember.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record QuestMemberPlannedRequest(
	@NotNull(message = "startDateTime can't be Null.") LocalDateTime startDateTime,
	@NotNull(message = "endDateTime can't be Null.") LocalDateTime endDateTime,
	Boolean autoReschedule
) {
}
