package kr.co.jeelee.kiwee.global.resolver;

import java.util.Map;
import java.util.function.Function;

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

	private final static Map<Class<?>, Function<Object, ?>> responseConverter = Map.of(
		Platform.class, (obj) -> PlatformSimpleResponse.from((Platform) obj),
		Channel.class, (obj) -> ChannelSimpleResponse.from((Channel) obj),
		Member.class, (obj) -> MemberSimpleResponse.from((Member) obj),
		Task.class, (obj) -> TaskResponse.from((Task) obj),
		Pledge.class, (obj) -> PledgeSimpleResponse.from((Pledge) obj),
		Content.class, (obj) -> ContentSimpleResponse.from((Content) obj),
		Reward.class, (obj) -> RewardSimpleResponse.from((Reward) obj),
		Badge.class, (obj) -> BadgeSimpleResponse.from((Badge) obj, 1)
	);

	@SuppressWarnings("unchecked")
	public static <T> T toResponse(Object domainObject) {
		if (domainObject == null) return null;

		Function<Object, ?> converter = responseConverter.get(domainObject.getClass());

		if (converter == null) {
			throw new CastErrorException(domainObject.getClass());
		}

		return (T) converter.apply(domainObject);
	}

}
