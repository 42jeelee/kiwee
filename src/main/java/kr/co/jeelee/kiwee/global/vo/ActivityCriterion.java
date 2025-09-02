package kr.co.jeelee.kiwee.global.vo;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.global.model.DomainType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class ActivityCriterion implements Serializable {

	@EqualsAndHashCode.Include
	@Column(name = "domain_type")
	private DomainType domainType;

	@EqualsAndHashCode.Include
	@Column(name = "domain_id")
	private UUID domainId;

	@EqualsAndHashCode.Include
	@Column(name = "activity_type")
	private ActivityType activityType;

	@Column(name = "activity_count")
	private int activityCount;

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
