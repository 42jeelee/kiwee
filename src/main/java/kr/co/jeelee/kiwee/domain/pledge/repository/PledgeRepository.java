package kr.co.jeelee.kiwee.domain.pledge.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.pledge.entity.Pledge;
import kr.co.jeelee.kiwee.global.model.TermType;

public interface PledgeRepository extends JpaRepository<Pledge, UUID> {

	Page<Pledge> findByProposerAndTermType(Member proposer, TermType termType, Pageable pageable);

	Page<Pledge> findByProposer(Member proposer, Pageable pageable);

	Page<Pledge> findByTermType(TermType termType, Pageable pageable);

}
