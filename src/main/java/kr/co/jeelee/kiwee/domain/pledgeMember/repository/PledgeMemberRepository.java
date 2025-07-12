package kr.co.jeelee.kiwee.domain.pledgeMember.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.pledge.entity.Pledge;
import kr.co.jeelee.kiwee.domain.pledgeMember.entity.PledgeMember;
import kr.co.jeelee.kiwee.domain.pledgeMember.model.PledgeStatusType;

public interface PledgeMemberRepository extends JpaRepository<PledgeMember, UUID> {

	boolean existsByMemberIdAndPledgeIdAndStatus(UUID memberId, UUID pledgeId, PledgeStatusType status);

	Page<PledgeMember> findByMember(Member member, Pageable pageable);

	Page<PledgeMember> findByPledge(Pledge pledge, Pageable pageable);

}
