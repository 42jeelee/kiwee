package kr.co.jeelee.kiwee.domain.badge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.badge.entity.Badge;
import kr.co.jeelee.kiwee.domain.badge.entity.BadgeLevel;

public interface BadgeLevelRepository extends JpaRepository<BadgeLevel, Long> {

	boolean existsByBadgeAndLevel(Badge badge, Integer level);

}
