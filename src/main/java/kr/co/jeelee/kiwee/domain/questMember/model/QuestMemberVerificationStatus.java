package kr.co.jeelee.kiwee.domain.questMember.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestMemberVerificationStatus {
	SUCCESS("성공"),
	HOLD("보류"),
	REJECTED("거부"),
	;
	private final String label;
}
