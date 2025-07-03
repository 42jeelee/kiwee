package kr.co.jeelee.kiwee.domain.Reward.listener;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import kr.co.jeelee.kiwee.domain.Reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.Reward.event.RewardEvent;
import kr.co.jeelee.kiwee.domain.Reward.service.RewardService;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.domain.memberActivity.service.MemberActivityService;
import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;
import kr.co.jeelee.kiwee.domain.rewardMember.service.RewardMemberService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RewardEventListener {

	private final MemberService memberService;
	private final RewardService rewardService;
	private final RewardMemberService rewardMemberService;
	private final MemberActivityService memberActivityService;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handle(RewardEvent rewardEvent) {
		Member awardee = memberService.getById(rewardEvent.awardeeId());

		MemberActivity activity = memberActivityService.getById(rewardEvent.activityId());

		List<RewardMember> rewardMembers = new ArrayList<>();

		List<Reward> rewards = rewardService.getByDomainAndTriggerType(
			rewardEvent.sourceType(),
			rewardEvent.sourceId(),
			rewardEvent.triggerType()
		);

		rewards.stream().filter(
			r -> r.getTriggerCount().equals(rewardEvent.triggerCount()))
			.forEach(r -> {
				RewardMember rewardMember = RewardMember.of(
					awardee,
					r,
					activity
				);

				rewardMembers.add(rewardMemberService.createRewardMember(rewardMember));
			}
		);

		memberActivityService.addRewards(activity.getId(), rewardMembers);
	}

}
