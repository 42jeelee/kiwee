package kr.co.jeelee.kiwee.domain.member.dao;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.jeelee.kiwee.domain.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

	Page<Member> findByNicknameContaining(String query, Pageable pageable);

	Boolean existsByNickname(String nickname);
	Boolean existsByEmail(String email);

}
