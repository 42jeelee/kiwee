package kr.co.jeelee.kiwee.domain.contentMember.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;
import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.contentMember.dto.response.ContentMemberStarResponse;
import kr.co.jeelee.kiwee.domain.contentMember.entity.ContentMember;
import kr.co.jeelee.kiwee.domain.member.entity.Member;

public interface ContentMemberRepository extends JpaRepository<ContentMember, Long> {

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

	Page<ContentMember> findByContent(Content content, Pageable pageable);
	Page<ContentMember> findByMember(Member member, Pageable pageable);
	Page<ContentMember> findByContentAndMember(Content content, Member member, Pageable pageable);

}
