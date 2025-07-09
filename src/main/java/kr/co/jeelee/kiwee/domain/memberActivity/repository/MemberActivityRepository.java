package kr.co.jeelee.kiwee.domain.memberActivity.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.domain.memberActivity.model.ActivityType;

public interface MemberActivityRepository extends JpaRepository<MemberActivity, UUID> {

	int countByActorIdAndSourceTypeAndType(UUID actorId, DomainType sourceType, ActivityType type);

	int countByActorIdAndSourceTypeAndSourceIdAndType(UUID actorId, DomainType sourceType, UUID sourceId, ActivityType type);

	Page<MemberActivity> findByActorId(UUID memberId, Pageable pageable);
}
