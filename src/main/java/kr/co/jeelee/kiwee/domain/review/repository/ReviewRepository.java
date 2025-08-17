package kr.co.jeelee.kiwee.domain.review.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;
import kr.co.jeelee.kiwee.domain.contentMember.entity.ContentMember;
import kr.co.jeelee.kiwee.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

	Page<Review> findByContentMember(ContentMember contentMemberId, Pageable pageable);

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
