package kr.co.jeelee.kiwee.domain.memberActivity.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.jeelee.kiwee.domain.content.model.ContentType;
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

	@Query("""
		SELECT COUNT(ma.id) FROM MemberActivity ma
		WHERE ma.actor.id = :actorId
		AND ma.type = :type
		AND ma.sourceId IN (
			SELECT c.id FROM Content c
			WHERE c.parent.id = :contentId
		)
	""")
	int countActivitiesInChildContents(
		@Param("actorId") UUID actorId,
		@Param("contentId") UUID contentId,
		@Param("type") ActivityType type
	);

	@Query("""
		SELECT COUNT(ma.id) FROM MemberActivity ma
		WHERE ma.actor.id = :actorId
		AND ma.type = :type
		AND ma.sourceId IN (
			SELECT c.id FROM Content c
			WHERE c.parent.id = :contentId
		)
		AND ma.createdAt BETWEEN :start AND :end
	""")
	int countActivitiesInChildContents(
		@Param("actorId") UUID actorId,
		@Param("contentId") UUID contentId,
		@Param("type") ActivityType type,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);

	@Query(value = """
		SELECT COUNT(ma.id) FROM membe_activities ma
		JOIN contents c ON ma.source_id = c.id
		WHERE ma.actor_id = :actorId
		AND ma.type = :type
		AND c.content_type = :contentType
	""", nativeQuery = true)
	int countActivitiesByContentType(
		@Param("actorId") UUID actorId,
		@Param("contentType") ContentType contentType,
		@Param("type") ActivityType type
	);

	@Query(value = """
		SELECT COUNT(ma.id) FROM member_activities ma
		JOIN contents c ON ma.source_id = c.id
		WHERE ma.actor_id = :actorId
		AND ma.type = :type
		AND c.content_type = :contentType
		AND ma.created_at BETWEEN :start AND :end
	""", nativeQuery = true)
	int countActivitiesByContentTypeAndTerm(
		@Param("actorId") UUID actorId,
		@Param("contentType") ContentType contentType,
		@Param("type") ActivityType type,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);

	@Query("""
		SELECT DISTINCT DATE(ma.createdAt) FROM MemberActivity ma
		WHERE ma.actor.id = :actorId
		AND ma.sourceType = :sourceType
		AND ma.sourceId = :sourceId
		AND ma.type = :type
		ORDER BY ma.createdAt DESC
		LIMIT :num
	""")
	List<LocalDate> findActivityDatesByCriterion(
		@Param("actorId") UUID actorId,
		@Param("sourceType") DomainType sourceType,
		@Param("sourceId") UUID sourceId,
		@Param("type") ActivityType type,
		@Param("num") int num
	);

	@Query("""
		SELECT DISTINCT DATE(ma.createdAt) FROM MemberActivity ma
		WHERE ma.actor.id = :actorId
		AND ma.sourceType = :sourceType
		AND ma.type = :type
		ORDER BY ma.createdAt DESC
		LIMIT :num
	""")
	List<LocalDate> findActivityDatesByCriterion(
		@Param("actorId") UUID actorId,
		@Param("sourceType") DomainType sourceType,
		@Param("type") ActivityType type,
		@Param("num") int num
	);

	@Query(value = """
		SELECT DISTINCT DATE(ma.created_at) FROM member_activities ma
		JOIN contents c ON ma.source_id = c.id
		WHERE ma.actor_id = :actorId
		AND ma.type = :type
		AND c.content_type = :contentType
		ORDER BY ma.created_at DESC
		LIMIT :num
	""", nativeQuery = true)
	List<LocalDate> findActivityDatesByCriterion(
		@Param("actorId") UUID actorId,
		@Param("contentType") ContentType contentType,
		@Param("type") ActivityType type,
		@Param("num") int num
	);

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
