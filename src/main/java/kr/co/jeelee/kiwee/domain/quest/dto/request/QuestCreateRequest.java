package kr.co.jeelee.kiwee.domain.quest.dto.request;

import java.time.LocalTime;
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
	@NotNull(message = "isRepeatable can't be Null.") Boolean isRepeatable,
	Integer maxSuccess, Integer maxRetryAllowed,
	@NotNull(message = "autoReschedule can't be Null.") Boolean autoReschedule,
	@NotNull(message = "rescheduleTerm can't be Null.") TermType rescheduleTerm
) {
}
