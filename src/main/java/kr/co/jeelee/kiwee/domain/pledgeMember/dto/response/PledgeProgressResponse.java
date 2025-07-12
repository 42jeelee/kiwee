package kr.co.jeelee.kiwee.domain.pledgeMember.dto.response;

import kr.co.jeelee.kiwee.global.dto.response.ActivityCriterionResponse;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import kr.co.jeelee.kiwee.global.vo.ActivityCriterion;

public record PledgeProgressResponse(
	ActivityCriterionResponse criterion, int successCount
) {
	public static PledgeProgressResponse from(
		ActivityCriterion criterion,
		int successCount,
		DomainObjectResolver resolver
	) {
		return new PledgeProgressResponse(
			ActivityCriterionResponse.from(criterion, resolver),
			successCount
		);
	}
}
