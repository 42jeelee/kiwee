package kr.co.jeelee.kiwee.domain.member.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.jeelee.kiwee.domain.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

	Page<Member> findByNicknameContaining(String query, Pageable pageable);

	Boolean existsByNickname(String nickname);
	Boolean existsByEmail(String email);

	@Query("""
		select m from Member m
		join fetch m.roles r
		join fetch r.permissions
		where m.id = :id
	""")
	Optional<Member> getWithRolesAndPermissionsById(@Param("id") UUID id);

}
