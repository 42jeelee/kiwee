package kr.co.jeelee.kiwee.domain.quest.dto.request;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.global.model.TermType;

public record QuestCreateRequest(
	@URL(message = "This is not in URL format.")
	@NotNull(message = "icon can't be Null.")
	String icon,
	@URL(message = "This is not in URL format.")
	@NotNull(message = "banner can't be Null.")
	String banner,
	@NotBlank(message = "title can't be Blank.") String title,
	@NotBlank(message = "description can't be Blank.") String description,
	@NotNull(message = "channelId can't be Null.") UUID channelId,
	LocalTime verifiableFrom, LocalTime verifiableUntil,
	@NotNull(message = "isThisInstant can't be Null.") Boolean isThisInstant,
	Duration completedLimit, Integer maxSuccess, Integer maxProgressCount, Integer maxRetryAllowed,
	@NotNull(message = "termType can't be Null.") TermType termType,
	List<Integer> activeDays,
	@NotNull(message = "minPerTerm can't be Null.") Integer minPerTerm,
	@NotNull(message = "maxSkipTerm can't be Null.") Integer maxSkipTerm,
	Integer maxAllowedFails
) {
}
