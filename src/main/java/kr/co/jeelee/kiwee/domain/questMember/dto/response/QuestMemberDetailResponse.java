package kr.co.jeelee.kiwee.domain.questMember.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.quest.dto.response.QuestSimpleResponse;
import kr.co.jeelee.kiwee.domain.questMember.entity.QuestMember;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberStatus;
import kr.co.jeelee.kiwee.global.model.MediaType;

public record QuestMemberDetailResponse(
	UUID id, QuestSimpleResponse quest, MemberSimpleResponse member,
	QuestMemberStatus status, LocalDateTime startDateTime, LocalDateTime endDateTime,
	LocalDateTime completedAt, MediaType mediaType, String mediaUrl, String message,
	Boolean autoReschedule, LocalDateTime updatedAt, LocalDateTime createdAt
) {
	public static QuestMemberDetailResponse from(QuestMember questMember) {
		return new QuestMemberDetailResponse(
			questMember.getId(),
			QuestSimpleResponse.from(questMember.getQuest()),
			MemberSimpleResponse.from(questMember.getMember()),
			questMember.getStatus(),
			questMember.getStartDateTime(),
			questMember.getEndDateTime(),
			questMember.getCompletedAt(),
			questMember.getMediaType(),
			questMember.getMediaUrl(),
			questMember.getMessage(),
			questMember.getAutoReschedule(),
			questMember.getUpdatedAt(),
			questMember.getCreatedAt()
		);
	}
}
