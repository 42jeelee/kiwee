package kr.co.jeelee.kiwee.domain.memberActivity.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.global.model.ActivityType;

public interface MemberActivityRepository extends JpaRepository<MemberActivity, UUID> {

	boolean existsByActorIdAndSourceTypeAndSourceIdIsNullAndTypeAndCreatedAtBetween(
		UUID actorId,
		DomainType sourceType,
		ActivityType type,
		LocalDateTime start,
		LocalDateTime end
	);

	boolean existsByActorIdAndSourceTypeAndSourceIdAndTypeAndCreatedAtBetween(
		UUID actorId,
		DomainType sourceType,
		UUID sourceId,
		ActivityType type,
		LocalDateTime start,
		LocalDateTime end
	);

	int countByActorIdAndSourceTypeAndSourceIdAndTypeAndCreatedAtBetween(
		UUID actorId,
		DomainType sourceType,
		UUID sourceId,
		ActivityType type,
		LocalDateTime start,
		LocalDateTime end
	);

	int countByActorIdAndSourceTypeAndSourceIdIsNullAndTypeAndCreatedAtBetween(
		UUID actorId,
		DomainType sourceType,
		ActivityType type,
		LocalDateTime start,
		LocalDateTime end
	);

	int countByActorIdAndSourceTypeAndType(UUID actorId, DomainType sourceType, ActivityType type);

	int countByActorIdAndSourceTypeAndSourceIdAndType(UUID actorId, DomainType sourceType, UUID sourceId, ActivityType type);

	Optional<MemberActivity> findFirstByActorIdAndSourceTypeAndSourceIdAndTypeInOrderByCreatedAtDesc(
		UUID actor,
		DomainType sourceType,
		UUID sourceId,
		List<ActivityType> activityTypes
	);

	Page<MemberActivity> findByActorId(UUID memberId, Pageable pageable);

	List<MemberActivity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

	List<MemberActivity> findByActorIdAndCreatedAtBetween(UUID memberId, LocalDateTime start, LocalDateTime end);

	List<MemberActivity> findByActorIdAndSourceTypeAndSourceIdAndTypeInAndCreatedAtBetweenOrderByCreatedAtAsc(
		UUID actorId,
		DomainType sourceType,
		UUID sourceId,
		List<ActivityType> activityTypes,
		LocalDateTime start,
		LocalDateTime end
	);
}
