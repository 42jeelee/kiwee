package kr.co.jeelee.kiwee.domain.reward.model;

import kr.co.jeelee.kiwee.global.model.DomainType;
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
