package kr.co.jeelee.kiwee.domain.notification.model;

import kr.co.jeelee.kiwee.global.model.DomainType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
	SYSTEM("시스템", DomainType.GLOBAL),
	INVITE("초대", DomainType.MEMBER),
	MENTION("언급", DomainType.MEMBER),
	CHANNEL("채널", DomainType.CHANNEL),
	REWARD("보상", DomainType.REWARD),
	;

	private final String label;
	private final DomainType publisherType;
}
