package kr.co.jeelee.kiwee.domain.badge.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.badge.entity.Badge;

public interface BadgeRepository extends JpaRepository<Badge, UUID> {

	Page<Badge> findByIsPublicTrue(Pageable pageable);

}
