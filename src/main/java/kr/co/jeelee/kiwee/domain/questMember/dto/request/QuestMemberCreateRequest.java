package kr.co.jeelee.kiwee.domain.questMember.dto.request;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record QuestMemberCreateRequest(
	@NotNull(message = "startDate can't be Null.") LocalDateTime startDate,
	LocalTime verifiableFrom, LocalTime verifiableUntil
) {
}
