package kr.co.jeelee.kiwee.domain.rewardMember.dto.response;

import kr.co.jeelee.kiwee.domain.Reward.dto.response.RewardSimpleResponse;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.memberActivity.dto.response.MemberActivityResponse;
import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;

public record RewardMemberDetailResponse(
	Long id, MemberSimpleResponse awardee, RewardSimpleResponse reward, MemberActivityResponse activity
) {
	public static RewardMemberDetailResponse from(
		RewardMember rewardMember, Object source, Object rewardObj
	) {
		return new RewardMemberDetailResponse(
			rewardMember.getId(),
			MemberSimpleResponse.from(rewardMember.getAwardee()),
			RewardSimpleResponse.from(rewardMember.getReward(), source, rewardObj),
			MemberActivityResponse.from(rewardMember.getActivity())
		);
	}
}
