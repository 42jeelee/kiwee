package kr.co.jeelee.kiwee.domain.questMember.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import kr.co.jeelee.kiwee.domain.questMember.entity.QuestMemberVerification;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberVerificationStatus;

public record QuestMemberVerificationResponse(
	LocalDate verifiedDate, String message, String contentType, String contentUrl,
	QuestMemberVerificationStatus status, LocalDateTime completedAt,
	LocalDateTime createdAt
) {
	public static QuestMemberVerificationResponse from(QuestMemberVerification questMemberVerification) {
		return new QuestMemberVerificationResponse(
			questMemberVerification.getVerifiedDate(),
			questMemberVerification.getMessage(),
			questMemberVerification.getContentType(),
			questMemberVerification.getContentUrl(),
			questMemberVerification.getStatus(),
			questMemberVerification.getCompletedAt(),
			questMemberVerification.getCreatedAt()
		);
	}
}
