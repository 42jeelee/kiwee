package kr.co.jeelee.kiwee.domain.questMember.dto.response;

import java.time.LocalDateTime;

import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.quest.dto.response.QuestSimpleResponse;
import kr.co.jeelee.kiwee.domain.questMember.entity.QuestMember;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberStatus;

public record QuestMemberSimpleResponse(
	QuestSimpleResponse quest, MemberSimpleResponse member,
	QuestMemberStatus status, String message, LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static QuestMemberSimpleResponse from(QuestMember questMember) {
		return new QuestMemberSimpleResponse(
			QuestSimpleResponse.from(questMember.getQuest()),
			MemberSimpleResponse.from(questMember.getMember()),
			questMember.getStatus(),
			questMember.getMessage(),
			questMember.getUpdatedAt(),
			questMember.getCreatedAt()
		);
	}
}
