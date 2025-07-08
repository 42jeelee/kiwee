package kr.co.jeelee.kiwee.domain.memberActivity.event;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.memberActivity.model.ActivityType;

public record MemberActivityEvent(
	UUID memberId,
	DomainType sourceType,
	UUID sourceId,
	ActivityType activityType
) {
	public static MemberActivityEvent of(UUID memberId, DomainType sourceType, UUID sourceId, ActivityType activityType) {
		return new MemberActivityEvent(memberId, sourceType, sourceId, activityType);
	}
}
