package kr.co.jeelee.kiwee.domain.Reward.model;

import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RewardType {
	NONE("없음", null),
	BADGE("배지", DomainType.BADGE),
	;

	private final String label;
	private final DomainType domainType;
}
