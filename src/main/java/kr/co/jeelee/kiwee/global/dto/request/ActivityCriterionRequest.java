package kr.co.jeelee.kiwee.global.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.vo.ActivityCriterion;

public record ActivityCriterionRequest(
	@NotNull(message = "domainType can't be Null.") DomainType domainType,
	UUID domainId,
	@NotNull(message = "activityType can't be Null.") ActivityType activityType,
	@Min(value = 1, message = "activityCount must be greater than or equal to 1.") int activityCount
) {
	public ActivityCriterion toDomain() {
		return new ActivityCriterion(domainType, domainId, activityType, activityCount);
	}
}
