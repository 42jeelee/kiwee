package kr.co.jeelee.kiwee.domain.memberActivity.listener;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import kr.co.jeelee.kiwee.domain.Reward.event.RewardEvent;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.memberActivity.event.MemberActivityEvent;
import kr.co.jeelee.kiwee.domain.memberActivity.service.MemberActivityService;
import kr.co.jeelee.kiwee.domain.pledgeMember.service.PledgeMemberStatusService;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberActivityListener {

	private final DomainObjectResolver domainObjectResolver;

	private final MemberService memberService;
	private final MemberActivityService memberActivityService;
	private final PledgeMemberStatusService pledgeMemberStatusService;

	private final ApplicationEventPublisher eventPublisher;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handle(MemberActivityEvent event) {
		Member actor = memberService.getById(event.memberId());
		String domainName = domainObjectResolver.resolveName(event.sourceType(), event.sourceId());
		String title = String.format(
			"%s '%s' 에 %s 하였습니다.",
			event.sourceType().getLabel(),
			domainName,
			event.activityType().getLabel()
		);

		UUID activityId =
			memberActivityService.log(actor, event.activityType(), event.sourceType(), event.sourceId(), title);

		pledgeMemberStatusService.progressActivity(activityId);
		eventPublisher.publishEvent(RewardEvent.of(activityId));
	}

}
