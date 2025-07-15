package kr.co.jeelee.kiwee.domain.task.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.task.entity.Task;
import kr.co.jeelee.kiwee.domain.task.model.TaskType;

public interface TaskRepository extends JpaRepository<Task, UUID> {

	boolean existsByMemberAndTaskTypeAndCreatedAtAfter(Member member, TaskType taskType, LocalDateTime createdAt);

	Page<Task> findByMember(Member member, Pageable pageable);

	Page<Task> findByTaskType(TaskType taskType, Pageable pageable);

	Page<Task> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

	Page<Task> findByTaskTypeAndCreatedAtBetween(TaskType taskType, LocalDateTime start, LocalDateTime end, Pageable pageable);

	Page<Task> findByMemberAndTaskType(Member member, TaskType taskType, Pageable pageable);

	Page<Task> findByMemberAndCreatedAtBetween(Member member, LocalDateTime start, LocalDateTime end, Pageable pageable);

	Page<Task> findByMemberAndTaskTypeAndCreatedAtBetween(
		Member member,
		TaskType taskType,
		LocalDateTime start,
		LocalDateTime end,
		Pageable pageable
	);

	@Query("""
		SELECT t FROM Task t
		WHERE t.taskType = :taskType
			AND FUNCTION('jsonb_extract_path_text', t.metadata, 'applicationId') = :application
	""")
	Page<Task> findPlayTaskByApplicationId(TaskType taskType, String applicationId, Pageable pageable);

}
