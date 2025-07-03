package kr.co.jeelee.kiwee.domain.quest.dto.response;

import java.util.List;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.quest.entity.Quest;
import kr.co.jeelee.kiwee.global.model.TermType;

public record QuestSimpleResponse(
	UUID id, String icon, String title, MemberSimpleResponse proposer,
	TermType termType, List<Integer> activeDays, Integer minPerTerm, Integer maxSkipTerm
) {
	public static QuestSimpleResponse from(Quest quest) {
		return new QuestSimpleResponse(
			quest.getId(),
			quest.getIcon(),
			quest.getTitle(),
			MemberSimpleResponse.from(quest.getProposer()),
			quest.getTermType(),
			quest.getActiveDays(),
			quest.getMinPerTerm(),
			quest.getMaxSkipTerm()
		);
	}
}
