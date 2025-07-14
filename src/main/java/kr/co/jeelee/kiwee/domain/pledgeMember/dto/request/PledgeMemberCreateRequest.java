package kr.co.jeelee.kiwee.domain.pledgeMember.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.global.dto.request.ActivityCriterionRequest;
import kr.co.jeelee.kiwee.global.vo.RepeatCondition;

public record PledgeMemberCreateRequest(
	@NotNull(message = "startAt can't be Null.") LocalDateTime startAt,
	LocalDateTime completedAt,
	List<@Valid ConditionRequest> conditions
) {
	public record ConditionRequest(
		@Valid ActivityCriterionRequest criterion,
		@Valid RepeatCondition condition
	) {}
}
