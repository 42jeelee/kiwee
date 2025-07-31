package kr.co.jeelee.kiwee.domain.reward.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.jeelee.kiwee.domain.reward.entity.Reward;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.model.ActivityType;

public interface RewardRepository extends JpaRepository<Reward, UUID> {

	Page<Reward> findByIsPublicTrue(Pageable pageable);

	Page<Reward> findByCondition_Criterion_DomainTypeAndCondition_Criterion_DomainIdAndIsPublicTrue(
		DomainType domainType,
		UUID domainId,
		Pageable pageable
	);

	List<Reward> findByCondition_Criterion_DomainTypeAndCondition_Criterion_DomainIdIsNullAndCondition_Criterion_ActivityType(
		DomainType domainType,
		ActivityType activityType
	);
	List<Reward> findByCondition_Criterion_DomainTypeAndCondition_Criterion_DomainIdAndCondition_Criterion_ActivityType(
		DomainType domainType,
		UUID domainId,
		ActivityType activityType
	);

}
