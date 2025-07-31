package kr.co.jeelee.kiwee.domain.reward.listener;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import kr.co.jeelee.kiwee.domain.reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.reward.event.RewardEvent;
import kr.co.jeelee.kiwee.domain.reward.service.RewardService;
import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.domain.memberActivity.service.MemberActivityService;
import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;
import kr.co.jeelee.kiwee.domain.rewardMember.service.RewardMemberService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RewardEventListener {

	private final RewardService rewardService;
	private final RewardMemberService rewardMemberService;
	private final MemberActivityService memberActivityService;

	@Async
	@EventListener
	public void handle(RewardEvent rewardEvent) {
		MemberActivity activity = memberActivityService.getById(rewardEvent.activityId());

		List<Reward> matchRewards = rewardService.getMatchRewards(activity);

		List<RewardMember> rewardMembers = getRewardMembers(matchRewards, activity);

		memberActivityService.addRewards(activity.getId(), rewardMembers);
	}

	private List<RewardMember> getRewardMembers(List<Reward> rewards, MemberActivity activity) {
		return rewards.stream()
			.map(r -> RewardMember.of(activity.getActor(), r, activity))
			.map(rewardMemberService::createRewardMember)
			.toList();
	}

}
