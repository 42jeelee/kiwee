package kr.co.jeelee.kiwee.domain.rewardMember.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;

public interface RewardMemberRepository extends JpaRepository<RewardMember, Long> {

	Page<RewardMember> findByAwardeeId(UUID awardeeId, Pageable pageable);

}
