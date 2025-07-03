package kr.co.jeelee.kiwee.domain.Reward.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.Reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.Reward.model.TriggerType;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;

public interface RewardRepository extends JpaRepository<Reward, UUID> {

	Page<Reward> findByIsPublicTrue(Pageable pageable);

	Page<Reward> findBySourceTypeAndSourceIdAndIsPublicTrue(DomainType sourceType, UUID sourceId, Pageable pageable);

	List<Reward> findBySourceTypeAndSourceIdAndTriggerType(DomainType domainType, UUID domainId, TriggerType triggerType);

}
