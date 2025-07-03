package kr.co.jeelee.kiwee.domain.questMember.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.questMember.entity.QuestMember;
import kr.co.jeelee.kiwee.domain.questMember.entity.QuestMemberVerification;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberVerificationStatus;

public interface QuestMemberVerificationRepository extends JpaRepository<QuestMemberVerification, Long> {

	Page<QuestMemberVerification> findByQuestMemberMemberId(UUID questMemberMemberId, Pageable pageable);

	Page<QuestMemberVerification> findByQuestMemberQuestId(UUID questMemberQuestId, Pageable pageable);

	List<QuestMemberVerification> findByQuestMemberAndStatusAndVerifiedDateBetween(
		QuestMember questMember,
		QuestMemberVerificationStatus status,
		LocalDate start,
		LocalDate end
	);

	int countByQuestMember(QuestMember questMember);

}
