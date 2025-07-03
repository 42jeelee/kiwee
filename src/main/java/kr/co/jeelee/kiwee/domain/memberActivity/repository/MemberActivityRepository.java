package kr.co.jeelee.kiwee.domain.memberActivity.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;

public interface MemberActivityRepository extends JpaRepository<MemberActivity, UUID> {

	Page<MemberActivity> findByActorId(UUID memberId, Pageable pageable);
}
