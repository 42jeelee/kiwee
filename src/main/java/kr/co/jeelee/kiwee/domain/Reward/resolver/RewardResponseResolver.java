package kr.co.jeelee.kiwee.domain.Reward.resolver;

import java.util.Map;
import java.util.function.Function;

import kr.co.jeelee.kiwee.domain.badge.dto.response.BadgeSimpleResponse;
import kr.co.jeelee.kiwee.domain.badge.entity.Badge;
import kr.co.jeelee.kiwee.global.exception.common.CastErrorException;

public class RewardResponseResolver {

	private final static Map<Class<?>, Function<Object, ?>> responseConverter = Map.of(
		Badge.class, obj -> BadgeSimpleResponse.from((Badge) obj, 1)
	);

	@SuppressWarnings("unchecked")
	public static <T> T toResponse(Object rewardObject) {
		if (rewardObject == null) return null;

		Function<Object, ?> converter = responseConverter.get(rewardObject.getClass());

		if (converter == null) {
			throw new CastErrorException(rewardObject.getClass());
		}

		return (T) converter.apply(rewardObject);
	}

}
