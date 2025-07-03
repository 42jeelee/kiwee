package kr.co.jeelee.kiwee.domain.questMember.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestMemberStatus {
	PLANNED("계획함"),
	IN_PROGRESS("진행중"),
	SUCCEEDED("성공"),
	FAILED("실패"),
	;

	private final String label;

}
