package kr.co.jeelee.kiwee.domain.quest.dto.response;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.quest.entity.Quest;
import kr.co.jeelee.kiwee.global.model.TermType;

public record QuestDetailResponse(
	UUID id, String icon, String banner, String title, String description,
	MemberSimpleResponse proposer, Boolean isThisInstant, Duration completedLimit,
	TermType termType, List<Integer> activeDays, Integer minPerTerm, Integer maxSkipTerm
) {
	public static QuestDetailResponse from(Quest quest) {
		return new QuestDetailResponse(
			quest.getId(),
			quest.getIcon(),
			quest.getBanner(),
			quest.getTitle(),
			quest.getDescription(),
			MemberSimpleResponse.from(quest.getProposer()),
			quest.getIsThisInstant(),
			quest.getCompletedLimit(),
			quest.getTermType(),
			quest.getActiveDays(),
			quest.getMinPerTerm(),
			quest.getMaxSkipTerm()
		);
	}
}
