package kr.co.jeelee.kiwee.domain.Reward.resolver;

import java.util.UUID;

import org.springframework.stereotype.Component;

import kr.co.jeelee.kiwee.domain.Reward.model.RewardType;
import kr.co.jeelee.kiwee.domain.badge.service.BadgeService;
import kr.co.jeelee.kiwee.global.exception.common.DomainNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RewardObjectResolver {

	private final BadgeService badgeService;

	public Object resolve(RewardType type, UUID id) {
		return switch (type) {
			case NONE -> null;
			case BADGE -> badgeService.getById(id);
			default -> new DomainNotFoundException();
		};
	}

	public <T> T resolve(RewardType type, UUID id, Class<T> clazz){
		Object rewardObject = resolve(type, id);

		if (!clazz.isInstance(rewardObject)) {
			throw new DomainNotFoundException();
		}

		return clazz.cast(rewardObject);
	}
}
