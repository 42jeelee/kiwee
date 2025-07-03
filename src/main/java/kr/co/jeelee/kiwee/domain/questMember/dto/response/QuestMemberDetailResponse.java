package kr.co.jeelee.kiwee.domain.questMember.dto.response;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.quest.dto.response.QuestSimpleResponse;
import kr.co.jeelee.kiwee.domain.questMember.entity.QuestMember;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberStatus;

public record QuestMemberDetailResponse(
	UUID id, QuestSimpleResponse quest, MemberSimpleResponse member,
	QuestMemberStatus status, LocalDateTime startDate, LocalDateTime endDate,
	LocalTime verifiableFrom, LocalTime verifiableUntil,
	LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static QuestMemberDetailResponse from(QuestMember questMember) {
		return new QuestMemberDetailResponse(
			questMember.getId(),
			QuestSimpleResponse.from(questMember.getQuest()),
			MemberSimpleResponse.from(questMember.getMember()),
			questMember.getStatus(),
			questMember.getStartDate(),
			questMember.getEndDate(),
			questMember.getVerifiableFrom(),
			questMember.getVerifiableUntil(),
			questMember.getUpdatedAt(),
			questMember.getCreatedAt()
		);
	}
}
