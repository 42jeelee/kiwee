package kr.co.jeelee.kiwee.domain.Reward.listener;

import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import kr.co.jeelee.kiwee.domain.Reward.entity.Reward;
import kr.co.jeelee.kiwee.domain.Reward.event.RewardEvent;
import kr.co.jeelee.kiwee.domain.Reward.service.RewardService;
import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.domain.memberActivity.service.MemberActivityService;
import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;
import kr.co.jeelee.kiwee.domain.rewardMember.service.RewardMemberService;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RewardEventListener {

	private final RewardService rewardService;
	private final RewardMemberService rewardMemberService;
	private final MemberActivityService memberActivityService;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handle(RewardEvent rewardEvent) {
		MemberActivity activity = memberActivityService.getById(rewardEvent.activityId());

		int generalCount = memberActivityService.getGeneralCount(activity.getId());
		int specificCount = memberActivityService.getSpecificCount(activity.getId());

		List<RewardMember> rewardMembers = new ArrayList<>();

		List<Reward> generalRewards = rewardService.getGeneralRewards(
			activity.getSourceType(),
			activity.getType()
		);

		List<Reward> specificRewards = rewardService.getSpecificRewards(
			activity.getSourceType(),
			activity.getSourceId(),
			activity.getType()
		);

		rewardMembers.addAll(getRewardMembers(generalRewards, generalCount, activity));
		rewardMembers.addAll(getRewardMembers(specificRewards, specificCount, activity));

		memberActivityService.addRewards(activity.getId(), rewardMembers);
	}

	private List<RewardMember> getRewardMembers(List<Reward> rewards, int activityCount, MemberActivity activity) {
		return rewards.stream()
			.filter(r -> (
				r.getActivityCount().equals(activityCount)
				&& (
					r.getActivityType() != ActivityType.END
					|| r.getDuration().compareTo(memberActivityService.getPlayDuration(activity)) <= 0
				)
			))
			.map(r -> RewardMember.of(activity.getActor(), r, activity))
			.map(rewardMemberService::createRewardMember)
			.toList();
	}

}
