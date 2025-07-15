package kr.co.jeelee.kiwee.global.vo;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.model.DomainType;

@Embeddable
public record ActivityCriterion(
	DomainType domainType, UUID domainId, ActivityType activityType, int activityCount
) implements Serializable {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof ActivityCriterion that)) return false;
		return this.domainType == that.domainType &&
			Objects.equals(this.domainId, that.domainId) &&
			this.activityType == that.activityType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(domainType, domainId, activityType);
	}

	public boolean equalsActivity(MemberActivity activity) {
		if (activity == null) return false;
		return this.domainType == activity.getSourceType() &&
			Objects.equals(this.domainId, activity.getSourceId()) &&
			this.activityType == activity.getType();
	}

	public boolean matchesType(ActivityCriterion that) {
		if (this == that) return true;
		return this.domainType == that.domainType &&
			(this.domainId == null || Objects.equals(this.domainId, that.domainId)) &&
			this.activityType == that.activityType;
	}

	public static ActivityCriterion of(
		DomainType domainType,
		UUID domainId,
		ActivityType activityType,
		int activityCount
	) {
		return new ActivityCriterion(domainType, domainId, activityType, activityCount);
	}

	public static ActivityCriterion of(DomainType domainType, UUID domainId, ActivityType activityType) {
		return new ActivityCriterion(domainType, domainId, activityType, 1);
	}

}
