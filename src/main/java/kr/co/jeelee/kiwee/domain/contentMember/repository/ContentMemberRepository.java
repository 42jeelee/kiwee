package kr.co.jeelee.kiwee.domain.contentMember.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;
import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberStarResponse;
import kr.co.jeelee.kiwee.domain.contentMember.entity.ContentMember;
import kr.co.jeelee.kiwee.domain.member.entity.Member;

public interface ContentMemberRepository extends JpaRepository<ContentMember, Long> {

	boolean existsByMemberIdAndContentId(UUID memberId, UUID contentId);
	boolean existsByContent_Parent_IdAndMember_Id(UUID parentId, UUID memberId);

	@Query("""
		SELECT new kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberStarResponse(
			CAST(COALESCE(AVG(cm.star), 0.0) AS double),
			COUNT(cm.id)
		) FROM ContentMember cm
		WHERE cm.content.id = :contentId
	""")
	ContentMemberStarResponse getAverageStarByContentId(@Param("contentId") UUID contentId);

	@EntityGraph(attributePaths = "reviews")
	Optional<ContentMember> findByContentAndMember(Content content, Member member);

	List<ContentMember> findByContent_Parent_IdAndMember_IdAndContent_ChildrenIdxGreaterThan(UUID parentId, UUID memberId, Long childrenIdx);

	Page<ContentMember> findByContent(Content content, Pageable pageable);
	Page<ContentMember> findByMember(Member member, Pageable pageable);
	Page<ContentMember> findByMemberAndContent_ContentTypeIn(Member member, Set<ContentType> contentTypes, Pageable pageable);

	@Query(value = """
    	WITH RECURSIVE descendants AS (
			SELECT id
			FROM contents
			WHERE id = :contentId
			UNION ALL
			SELECT c.id
			FROM contents c
			JOIN descendants d ON c.parent_id = d.id
		)
		SELECT cm.*
		FROM (
			SELECT DISTINCT ON (cm.member_id) cm.*
			FROM content_members cm
			WHERE cm.content_id IN (SELECT id FROM descendants)
			ORDER BY cm.member_id, cm.updated_at DESC, cm.id DESC
		) cm
		ORDER BY cm.updated_at DESC, cm.id DESC
  	""",
	countQuery = """
		WITH RECURSIVE descendants AS (
			SELECT id
			FROM contents
			WHERE id = :contentId
		UNION ALL
			SELECT c.id
			FROM contents c
			JOIN descendants d ON c.parent_id = d.id
		)
		SELECT COUNT(*)
		FROM (
			SELECT DISTINCT cm.member_id
			FROM content_members cm
			WHERE cm.content_id IN (SELECT id FROM descendants)
		) s
	""", nativeQuery = true)
	Page<ContentMember> findLatestPerMemberIncludingDescendants(
		@Param("contentId") UUID contentId,
		Pageable pageable
	);

}
