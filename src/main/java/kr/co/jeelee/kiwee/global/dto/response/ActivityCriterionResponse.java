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
			activityCriterion.getDomainType(),
			DomainResponseResolver.toResponse(
				resolver.resolve(activityCriterion.getDomainType(), activityCriterion.getDomainId())
			),
			activityCriterion.getActivityType(),
			activityCriterion.getActivityCount()
		);
	}
}
