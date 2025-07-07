package kr.co.jeelee.kiwee.domain.quest.dto.response;

import java.util.UUID;

import kr.co.jeelee.kiwee.domain.channel.dto.response.ChannelSimpleResponse;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.quest.entity.Quest;

public record QuestSimpleResponse(
	UUID id, String icon, String title, ChannelSimpleResponse channel, MemberSimpleResponse proposer
) {
	public static QuestSimpleResponse from(Quest quest) {
		return new QuestSimpleResponse(
			quest.getId(),
			quest.getIcon(),
			quest.getTitle(),
			ChannelSimpleResponse.from(quest.getChannel()),
			MemberSimpleResponse.from(quest.getProposer())
		);
	}
}
