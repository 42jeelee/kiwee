package kr.co.jeelee.kiwee.domain.contentMember.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ContentMemberCreateRequest(
	LocalDateTime startAt,
	@NotNull(message = "isCompleted can't be Null.") Boolean isCompleted,
	@Min(value = -2, message = "recommended must be -2 or greater.")
	@Max(value = 2, message = "recommended must be 2 or less.")
	Integer recommended,
	String recommendReason,
	@Min(value = 0, message = "star must be 0 or greater.")
	@Max(value = 10, message = "star must be 10 or less.")
	@NotNull(message = "star can't be Null.")
	Double star,
	Long consumedAmount
) {
}
