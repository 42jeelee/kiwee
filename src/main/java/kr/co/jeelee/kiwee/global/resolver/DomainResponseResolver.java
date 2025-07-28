package kr.co.jeelee.kiwee.global.resolver;

import java.util.Map;
import java.util.function.BiFunction;

import kr.co.jeelee.kiwee.domain.reward.dto.response.RewardSimpleResponse;
import kr.co.jeelee.kiwee.domain.reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.badge.dto.response.BadgeSimpleResponse;
import kr.co.jeelee.kiwee.domain.badge.entity.Badge;
import kr.co.jeelee.kiwee.domain.channel.dto.response.ChannelSimpleResponse;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.content.dto.response.ContentSimpleResponse;
import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.platform.dto.response.PlatformSimpleResponse;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;
import kr.co.jeelee.kiwee.domain.pledge.dto.response.PledgeSimpleResponse;
import kr.co.jeelee.kiwee.domain.pledge.entity.Pledge;
import kr.co.jeelee.kiwee.domain.task.dto.response.TaskResponse;
import kr.co.jeelee.kiwee.domain.task.entity.Task;
import kr.co.jeelee.kiwee.global.exception.common.CastErrorException;

public class DomainResponseResolver {

	private final static Map<Class<?>, BiFunction<Object, DomainObjectResolver, ?>> responseConverter = Map.of(
		Platform.class, (obj, resolver) -> PlatformSimpleResponse.from((Platform) obj),
		Channel.class, (obj, resolver) -> ChannelSimpleResponse.from((Channel) obj),
		Member.class, (obj, resolver) -> MemberSimpleResponse.from((Member) obj),
		Task.class, (obj, resolver) -> TaskResponse.from((Task) obj),
		Pledge.class, (obj, resolver) -> PledgeSimpleResponse.from((Pledge) obj),
		Content.class, (obj, resolver) -> ContentSimpleResponse.from((Content) obj),
		Reward.class, (obj, resolver) -> RewardSimpleResponse.from((Reward) obj, resolver),
		Badge.class, (obj, resolver) -> BadgeSimpleResponse.from((Badge) obj, 1)
	);

	@SuppressWarnings("unchecked")
	public static <T> T toResponse(Object domainObject, DomainObjectResolver resolver) {
		if (domainObject == null) return null;

		BiFunction<Object, DomainObjectResolver, ?> converter = responseConverter.get(domainObject.getClass());

		if (converter == null) {
			throw new CastErrorException(domainObject.getClass());
		}

		return (T) converter.apply(domainObject, resolver);
	}

	public static <T> T toResponse(Object domainObject) {
		return toResponse(domainObject, null);
	}

}
