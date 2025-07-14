package kr.co.jeelee.kiwee.domain.pledge.dto.request;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.domain.pledgeMember.dto.request.PledgeMemberCreateRequest;
import kr.co.jeelee.kiwee.global.model.RepeatConditionField;
import kr.co.jeelee.kiwee.global.vo.RepeatCondition;
import kr.co.jeelee.kiwee.global.dto.request.ActivityCriterionRequest;
import kr.co.jeelee.kiwee.global.model.TermType;

public record PledgeCreateRequest(
	@NotBlank(message = "title can't be Blank.") String title,
	@NotBlank(message = "description can't be Blank.") String description,
	@Valid @NotEmpty(message = "rules can't be Empty.") List<PledgeRuleRequest> rules,
	Duration completedLimit,
	@NotNull(message = "termType can't be Null.") TermType termType,
	@Valid @NotNull(message = "joinRequest can't be Null.") PledgeMemberCreateRequest joinRequest
) {
	public record PledgeRuleRequest(
		@Valid @NotNull(message = "criteria can't be Null.") ActivityCriterionRequest criterion,
		@Valid RepeatCondition condition,
		Set<RepeatConditionField> allowedCustomFields
	) {}
}
