package kr.co.jeelee.kiwee.domain.quest.dto.request;

import java.time.LocalTime;

import org.hibernate.validator.constraints.URL;

import kr.co.jeelee.kiwee.global.model.TermType;

public record QuestUpdateRequest(
	@URL(message = "This is not in URL format.") String icon,
	@URL(message = "This is not in URL format.") String banner,
	String title, String description, LocalTime verifiableFrom, LocalTime verifiableUntil, Boolean isThisInstant,
	Boolean isRepeatable, Integer maxSuccess, Integer maxRetryAllowed, Boolean isActive,
	Boolean autoReschedule, TermType rescheduleTerm
) {
}
