package kr.co.jeelee.kiwee.domain.pledge.dto.response;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.global.dto.response.ActivityCriterionResponse;
import kr.co.jeelee.kiwee.global.model.RepeatConditionField;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import kr.co.jeelee.kiwee.global.vo.RepeatCondition;
import kr.co.jeelee.kiwee.domain.pledge.entity.Pledge;
import kr.co.jeelee.kiwee.global.model.TermType;

public record PledgeDetailResponse(
	UUID id, String title, String description, MemberSimpleResponse proposer,
	List<ActivityCriterionResponse> criteria,
	Duration completedLimit, TermType termType, RepeatCondition condition,
	Set<RepeatConditionField> allowedCustomFields
) {
	public static PledgeDetailResponse from(Pledge pledge, DomainObjectResolver resolver) {
		return new PledgeDetailResponse(
			pledge.getId(),
			pledge.getTitle(),
			pledge.getDescription(),
			MemberSimpleResponse.from(pledge.getProposer()),
			pledge.getCriteria().stream()
				.map(c -> ActivityCriterionResponse.from(c, resolver))
				.toList(),
			pledge.getCompletedLimit(),
			pledge.getTermType(),
			pledge.getCondition(),
			pledge.getAllowedCustomFields()
		);
	}
}
