package kr.co.jeelee.kiwee.domain.Reward.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import kr.co.jeelee.kiwee.global.exception.common.CastErrorException;

public class RewardResponseResolver {

	private final static Map<Class<?>, Function<Object, ?>> responseConverter = Map.of(

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
