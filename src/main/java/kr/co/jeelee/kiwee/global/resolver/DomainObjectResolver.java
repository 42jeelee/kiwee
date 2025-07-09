package kr.co.jeelee.kiwee.global.resolver;

import java.util.UUID;

import org.springframework.stereotype.Component;

import kr.co.jeelee.kiwee.domain.Reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.Reward.repository.RewardRepository;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.badge.entity.Badge;
import kr.co.jeelee.kiwee.domain.badge.repository.BadgeRepository;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.channel.repository.ChannelRepository;
import kr.co.jeelee.kiwee.domain.content.entity.Content;
import kr.co.jeelee.kiwee.domain.content.repository.ContentRepository;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.repository.MemberRepository;
import kr.co.jeelee.kiwee.domain.platform.entity.Platform;
import kr.co.jeelee.kiwee.domain.platform.repository.PlatformRepository;
import kr.co.jeelee.kiwee.domain.quest.entity.Quest;
import kr.co.jeelee.kiwee.domain.quest.repository.QuestRepository;
import kr.co.jeelee.kiwee.domain.task.repository.TaskRepository;
import kr.co.jeelee.kiwee.global.exception.common.CastErrorException;
import kr.co.jeelee.kiwee.global.exception.common.DomainNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DomainObjectResolver {

	private final PlatformRepository platformRepository;
	private final ChannelRepository channelRepository;
	private final MemberRepository memberRepository;
	private final QuestRepository questRepository;
	private final TaskRepository taskRepository;
	private final ContentRepository contentRepository;
	private final RewardRepository rewardRepository;
	private final BadgeRepository badgeRepository;

	public Object resolve(DomainType domain, UUID id){
		return switch (domain) {
			case GLOBAL -> null;
			case PLATFORM -> platformRepository.findById(id).orElseThrow(DomainNotFoundException::new);
			case CHANNEL -> channelRepository.findById(id).orElseThrow(DomainNotFoundException::new);
			case MEMBER -> memberRepository.findById(id).orElseThrow(DomainNotFoundException::new);
			case QUEST -> questRepository.findById(id).orElseThrow(DomainNotFoundException::new);
			case TASK -> taskRepository.findById(id).orElseThrow(DomainNotFoundException::new);
			case CONTENT -> contentRepository.findById(id).orElseThrow(DomainNotFoundException::new);
			case REWARD -> rewardRepository.findById(id).orElseThrow(DomainNotFoundException::new);
			case BADGE -> badgeRepository.findById(id).orElseThrow(DomainNotFoundException::new);
			default -> throw new DomainNotFoundException();
		};
	}

	public <T> T resolve(DomainType domain, UUID id, Class<T> clazz){
		Object domainObject = resolve(domain, id);

		if (!clazz.isInstance(domainObject)) {
			throw new CastErrorException(clazz);
		}

		return clazz.cast(domainObject);
	}

	public String resolveName(DomainType domain, UUID id) {
		return switch (domain) {
			case GLOBAL -> null;
			case PLATFORM -> platformRepository.findById(id).map(Platform::getName).orElseThrow(DomainNotFoundException::new);
			case CHANNEL -> channelRepository.findById(id).map(Channel::getName).orElseThrow(DomainNotFoundException::new);
			case MEMBER -> memberRepository.findById(id).map(Member::getName).orElseThrow(DomainNotFoundException::new);
			case QUEST -> questRepository.findById(id).map(Quest::getTitle).orElseThrow(DomainNotFoundException::new);
			case TASK -> taskRepository.findById(id).map(t -> t.getTaskType().name()).orElseThrow(DomainNotFoundException::new);
			case CONTENT -> contentRepository.findById(id).map(Content::getTitle).orElseThrow(DomainNotFoundException::new);
			case REWARD -> rewardRepository.findById(id).map(Reward::getTitle).orElseThrow(DomainNotFoundException::new);
			case BADGE -> badgeRepository.findById(id).map(Badge::getName).orElseThrow(DomainNotFoundException::new);
			default -> throw new DomainNotFoundException();
		};
	}

}
