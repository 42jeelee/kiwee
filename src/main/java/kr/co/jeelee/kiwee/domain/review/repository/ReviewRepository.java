package kr.co.jeelee.kiwee.domain.review.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;
import kr.co.jeelee.kiwee.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

	@Query(value = """
		WITH RECURSIVE tree AS (
			SELECT c.id AS content_id, 0 AS depth
			FROM content_members cm0
			JOIN contents c ON c.id = cm0.content_id
			WHERE cm0.id = :contentMemberId
		  UNION ALL
			SELECT c2.id AS content_id, tree.depth + 1
			FROM tree
			JOIN contents c2 ON c2.parent_id = tree.content_id
		)
		SELECT r.*
		FROM reviews r
		JOIN content_members cm ON cm.id = r.content_member_id
		WHERE cm.content_id IN (SELECT content_id FROM tree)
		ORDER BY r.created_at DESC
    """,
	countQuery = """
		WITH RECURSIVE tree AS (
			SELECT c.id AS content_id, 0 AS depth
			FROM content_members cm0
			JOIN contents c ON c.id = cm0.content_id
			WHERE cm0.id = :contentMemberId
		  UNION ALL
			SELECT c2.id AS content_id, tree.depth + 1
			FROM tree
			JOIN contents c2 ON c2.parent_id = tree.content_id
		)
		SELECT COUNT(*)
		FROM reviews r
		JOIN content_members cm ON cm.id = r.content_member_id
		WHERE cm.content_id IN (SELECT content_id FROM tree)
    """,
	nativeQuery = true
	)
	Page<Review> findByContentMemberIncludesChildren(@Param("contentMemberId") Long contentMemberId, Pageable pageable);

	Page<Review> findByContentMemberId(Long contentMemberId, Pageable pageable);

	Page<Review> findByContentMember_ContentId(UUID contentId, Pageable pageable);

	Page<Review> findByContentMember_MemberId(UUID memberId, Pageable pageable);

	Page<Review> findByContentMember_ContentIdAndConsumedAmount(UUID contentId, Long consumedAmount, Pageable pageable);

	@Query("""
		SELECT DISTINCT r.consumedAmount FROM Review r
		JOIN r.contentMember cm
		WHERE cm.content.id = :contentId
	""")
	List<Long> findDistinctConsumedAmountByContentId(@Param("contentId") UUID contentId);

}
