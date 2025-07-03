package kr.co.jeelee.kiwee.domain.quest.dto.request;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import org.hibernate.validator.constraints.URL;

import kr.co.jeelee.kiwee.global.model.TermType;

public record QuestUpdateRequest(
	@URL(message = "This is not in URL format.") String icon,
	@URL(message = "This is not in URL format.") String banner,
	String title, String description, LocalTime verifiableFrom, LocalTime verifiableUntil, Boolean isThisInstant,
	Duration completedLimit, Integer maxSuccess, Integer maxProgressCount,
	Integer maxRetryAllowed, Boolean isActive, TermType termType, List<Integer> activeDays,
	Integer minPerTerm, Integer maxSkipTerm, Integer maxAllowedFails
) {
}
