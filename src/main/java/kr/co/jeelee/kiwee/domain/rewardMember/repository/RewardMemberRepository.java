package kr.co.jeelee.kiwee.domain.rewardMember.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;

public interface RewardMemberRepository extends JpaRepository<RewardMember, Long> {

	@Query(value = """
		SELECT DATE(rm.created_at) FROM reward_members rm
		WHERE rm.awardee_id = :awardeeId
		AND rm.reward_id = :rewardId
		ORDER BY rm.created_at DESC
		LIMIT 1
	""", nativeQuery = true)
	Optional<LocalDate> findLastGetDatesByAwardeeIdAndRewardId(
		@Param("awardeeId") UUID awardeeId,
		@Param("rewardId") UUID rewardId
	);

	Page<RewardMember> findByAwardeeId(UUID awardeeId, Pageable pageable);

}
