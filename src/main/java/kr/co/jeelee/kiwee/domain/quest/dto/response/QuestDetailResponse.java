package kr.co.jeelee.kiwee.domain.quest.dto.response;

import java.time.LocalTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.channel.dto.response.ChannelSimpleResponse;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.quest.entity.Quest;
import kr.co.jeelee.kiwee.global.model.TermType;

public record QuestDetailResponse(
	UUID id, String icon, String banner, String title, String description,
	ChannelSimpleResponse channel, MemberSimpleResponse proposer,
	LocalTime verifiableFrom, LocalTime verifiableUtil, Boolean isThisInstant,
	Boolean isRepeatable, Integer maxSuccess, Integer maxRetryAllowed, Boolean isActive,
	Boolean autoReschedule, TermType rescheduleTerm
) {
	public static QuestDetailResponse from(Quest quest) {
		return new QuestDetailResponse(
			quest.getId(),
			quest.getIcon(),
			quest.getBanner(),
			quest.getTitle(),
			quest.getDescription(),
			ChannelSimpleResponse.from(quest.getChannel()),
			MemberSimpleResponse.from(quest.getProposer()),
			quest.getVerifiableFrom(),
			quest.getVerifiableUntil(),
			quest.getIsThisInstant(),
			quest.getIsRepeatable(),
			quest.getMaxSuccess(),
			quest.getMaxRetryAllowed(),
			quest.getIsActive(),
			quest.getAutoReschedule(),
			quest.getRescheduleTerm()
		);
	}
}
