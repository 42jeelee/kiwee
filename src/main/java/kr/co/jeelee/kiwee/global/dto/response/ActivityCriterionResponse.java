package kr.co.jeelee.kiwee.global.dto.response;

import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import kr.co.jeelee.kiwee.global.resolver.DomainResponseResolver;
import kr.co.jeelee.kiwee.global.vo.ActivityCriterion;

public record ActivityCriterionResponse(
	DomainType domainType, Object domain, ActivityType activityType, int activityCount
) {
	public static ActivityCriterionResponse from(ActivityCriterion activityCriterion, DomainObjectResolver resolver) {
		return new ActivityCriterionResponse(
			activityCriterion.domainType(),
			DomainResponseResolver.toResponse(
				resolver.resolve(activityCriterion.domainType(), activityCriterion.domainId())
			),
			activityCriterion.activityType(),
			activityCriterion.activityCount()
		);
	}
}
