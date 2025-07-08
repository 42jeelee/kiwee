package kr.co.jeelee.kiwee.domain.task.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.task.entity.Task;
import kr.co.jeelee.kiwee.domain.task.model.TaskType;

public interface TaskRepository extends JpaRepository<Task, UUID> {

	boolean existsByChannelAndMemberAndTaskTypeAndCreatedAtAfter(Channel channel, Member member, TaskType taskType, LocalDateTime createdAt);

	Page<Task> findByChannel(Channel channel, Pageable pageable);

	Page<Task> findByChannelAndMember(Channel channel, Member member, Pageable pageable);

	Page<Task> findByChannelAndTaskType(Channel channel, TaskType taskType, Pageable pageable);

	Page<Task> findByChannelAndCreatedAtBetween(Channel channel, LocalDateTime start, LocalDateTime end, Pageable pageable);

	Page<Task> findByChannelAndTaskTypeAndCreatedAtBetween(Channel channel, TaskType taskType, LocalDateTime start, LocalDateTime end, Pageable pageable);

	Page<Task> findByChannelAndMemberAndTaskType(Channel channel, Member member, TaskType taskType, Pageable pageable);

	Page<Task> findByChannelAndMemberAndCreatedAtBetween(Channel channel, Member member, LocalDateTime start, LocalDateTime end, Pageable pageable);

	Page<Task> findByChannelAndMemberAndTaskTypeAndCreatedAtBetween(
		Channel channel,
		Member member,
		TaskType taskType,
		LocalDateTime start,
		LocalDateTime end,
		Pageable pageable
	);

	@Query("""
		SELECT t FROM Task t
		WHERE t.channel = :channel
			AND t.taskType = :taskType
			AND FUNCTION('jsonb_extract_path_text', t.metadata, 'contentId') = :contentId
	""")
	Page<Task> findChannelReviewTaskByContentId(Channel channel, TaskType taskType, String contentId, Pageable pageable);

}
