package kr.co.jeelee.kiwee.domain.reputations.dao;

import java.time.YearMonth;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.reputations.entity.Reputation;
import kr.co.jeelee.kiwee.domain.reputations.projection.ReputationStatProjection;

@Repository
public interface ReputationsRepository extends JpaRepository<Reputation, Long> {

	@Query(value = """
		WITH aggregates AS (
			SELECT
				m.id,
				m.nickname,
				m.avatar_url,
				m.level,
				m.exp,
				COUNT(*) FILTER (WHERE r.is_up = TRUE) AS up_votes,
				COUNT(*) FILTER (WHERE r.is_up = FALSE) AS down_votes
			FROM
				members m
			LEFT JOIN reputations r ON m.id = r.receiver_id AND r.year_month = :yearMonth
			GROUP BY m.id
		)
		SELECT
			*,
			(a.up_votes - a.down_votes) AS net_score,
			DENSE_RANK() OVER (ORDER BY (a.up_votes - a.down_votes) DESC) rank
		FROM
			aggregates a
		ORDER BY rank
	""",
	countQuery = """
		SELECT COUNT(*)
		FROM (
			SELECT m.id
			FROM members m
				LEFT JOIN reputations r
				ON m.id = r.receiver_id AND r.year_month = :yearMonth
			GROUP BY m.id
		) count_query
	""",
	nativeQuery = true)
	Page<ReputationStatProjection> getRankingsByMonth(@Param("yearMonth") String yearMonth, Pageable pageable);

	@Query(value = """
		WITH aggregates AS (
			SELECT
				m.id,
				m.nickname,
				m.avatar_url,
				m.level,
				m.exp,
				COUNT(*) FILTER (WHERE r.is_up = TRUE) AS up_votes,
				COUNT(*) FILTER (WHERE r.is_up = FALSE) AS down_votes
			FROM
				members m
					LEFT JOIN reputations r ON m.id = r.receiver_id AND r.year_month = :yearMonth
			GROUP BY m.id
		),
		ranked AS (
			SELECT
				a.*,
				(a.up_votes - a.down_votes) AS net_score,
				DENSE_RANK() OVER (ORDER BY (a.up_votes - a.down_votes) DESC) rank
			FROM
				aggregates a
		)
		SELECT
			*
		FROM ranked
		WHERE id = :memberId;
	""", nativeQuery = true)
	Optional<ReputationStatProjection> getStatByMemberIdAndYearMonth(
		@Param("memberId") UUID memberId,
		@Param("yearMonth") String yearMonth
	);

	Page<Reputation> getByGiver(Member giver, Pageable pageable);

	boolean existsByGiverAndYearMonth(Member giver, YearMonth yearMonth);

}
