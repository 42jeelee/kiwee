package kr.co.jeelee.kiwee.domain.pledgeMember.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PledgeStatusType {
	PLANNED("계획"),
	IN_PROGRESS("진행"),
	SUCCESS("성공"),
	FAILED("실패"),
	;
	private final String label;
}
