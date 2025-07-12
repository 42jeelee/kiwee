package kr.co.jeelee.kiwee.domain.pledgeMember.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.global.vo.RepeatCondition;

public record PledgeMemberCreateRequest(
	@NotNull(message = "startAt can't be Null.") LocalDateTime startAt,
	@Valid RepeatCondition condition
) {
}
