package kr.co.jeelee.kiwee.global.resolver;

import java.util.UUID;

import org.springframework.stereotype.Component;

import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.channel.service.ChannelService;
import kr.co.jeelee.kiwee.domain.quest.service.QuestService;
import kr.co.jeelee.kiwee.global.exception.common.CastErrorException;
import kr.co.jeelee.kiwee.global.exception.common.DomainNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DomainObjectResolver {

	private final ChannelService channelService;
	private final QuestService questService;

	public Object resolve(DomainType domain, UUID id){
		return switch (domain) {
			case CHANNEL -> channelService.getById(id);
			case QUEST -> questService.getById(id);
			default -> new DomainNotFoundException();
		};
	}

	public <T> T resolve(DomainType domain, UUID id, Class<T> clazz){
		Object domainObject = resolve(domain, id);

		if (!clazz.isInstance(domainObject)) {
			throw new CastErrorException(clazz);
		}

		return clazz.cast(domainObject);
	}

}
