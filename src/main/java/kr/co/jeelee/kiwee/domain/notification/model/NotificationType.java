package kr.co.jeelee.kiwee.domain.notification.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
	SYSTEM("시스템"),
	INVITE("초대"),
	MENTION("언급"),
	CHANNEL("채널"),
	QUEST("퀘스트"),
	REWARD("보상"),
	;

	private final String label;
}
