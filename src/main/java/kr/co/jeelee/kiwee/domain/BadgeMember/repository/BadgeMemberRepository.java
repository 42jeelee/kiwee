package kr.co.jeelee.kiwee.domain.BadgeMember.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.BadgeMember.entity.BadgeMember;
import kr.co.jeelee.kiwee.domain.badge.entity.Badge;
import kr.co.jeelee.kiwee.domain.member.entity.Member;

public interface BadgeMemberRepository extends JpaRepository<BadgeMember, Long> {

	Optional<BadgeMember> findByBadgeAndMember(Badge badge, Member member);

	Page<BadgeMember> findByBadge(Badge badge, Pageable pageable);

	Page<BadgeMember> findByMember(Member member, Pageable pageable);

}
