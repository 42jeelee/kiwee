package kr.co.jeelee.kiwee.global.resolver;

import java.util.Map;
import java.util.function.Function;

import kr.co.jeelee.kiwee.domain.channel.dto.response.ChannelSimpleResponse;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.quest.dto.response.QuestSimpleResponse;
import kr.co.jeelee.kiwee.domain.quest.entity.Quest;
import kr.co.jeelee.kiwee.global.exception.common.CastErrorException;

public class DomainResponseResolver {

	private final static Map<Class<?>, Function<Object, ?>> responseConverter = Map.of(
		Channel.class, obj -> ChannelSimpleResponse.from((Channel) obj),
		Quest.class, obj -> QuestSimpleResponse.from((Quest) obj)
	);

	@SuppressWarnings("unchecked")
	public static <T> T toResponse(Object domainObject) {
		Function<Object, ?> converter = responseConverter.get(domainObject.getClass());

		if (converter == null) {
			throw new CastErrorException(domainObject.getClass());
		}

		return (T) converter.apply(domainObject);
	}

}
