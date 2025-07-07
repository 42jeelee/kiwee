package kr.co.jeelee.kiwee.domain.questMember.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.quest.entity.Quest;
import kr.co.jeelee.kiwee.domain.questMember.entity.QuestMember;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberStatus;
import kr.co.jeelee.kiwee.global.model.TermType;

public interface QuestMemberRepository extends JpaRepository<QuestMember, UUID> {

	Optional<QuestMember> findByQuestAndMember(Quest quest, Member member);
	Optional<QuestMember> findByQuestAndMemberAndStatus(Quest quest, Member member, QuestMemberStatus status);

	Page<QuestMember> findByMemberAndStatus(Member member, QuestMemberStatus status, Pageable pageable);

	Page<QuestMember> findByQuestAndStatus(Quest quest, QuestMemberStatus status, Pageable pageable);

	List<QuestMember> findByStatus(QuestMemberStatus status);
	List<QuestMember> findByStatusAndEndDateTimeBefore(QuestMemberStatus status, LocalDateTime endDateTime);

	boolean existsByQuestAndMemberAndStatus(Quest quest, Member member, QuestMemberStatus status);

}
