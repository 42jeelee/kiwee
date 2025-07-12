package kr.co.jeelee.kiwee.global.dto.response;

import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import kr.co.jeelee.kiwee.global.resolver.DomainResponseResolver;
import kr.co.jeelee.kiwee.global.vo.ActivityCriterion;

public record ActivityCriterionResponse(
	Object domain, ActivityType activityType, int activityCount
) {
	public static ActivityCriterionResponse from(ActivityCriterion activityCriterion, DomainObjectResolver resolver) {
		return new ActivityCriterionResponse(
			DomainResponseResolver.toResponse(
				resolver.resolve(activityCriterion.domainType(), activityCriterion.domainId())
			),
			activityCriterion.activityType(),
			activityCriterion.activityCount()
		);
	}
}
