package kr.co.jeelee.kiwee.domain.Reward.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.Reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.memberActivity.model.ActivityType;

public interface RewardRepository extends JpaRepository<Reward, UUID> {

	Page<Reward> findByIsPublicTrue(Pageable pageable);

	Page<Reward> findBySourceTypeAndSourceIdAndIsPublicTrue(DomainType sourceType, UUID sourceId, Pageable pageable);

	List<Reward> findBySourceTypeAndSourceIdIsNullAndActivityType(DomainType sourceType, ActivityType activityType);
	List<Reward> findBySourceTypeAndSourceIdAndActivityType(DomainType domainType, UUID domainId, ActivityType activityType);

}
