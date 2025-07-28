package kr.co.jeelee.kiwee.domain.rewardMember.dto.response;

import kr.co.jeelee.kiwee.domain.reward.model.RewardType;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;

public record RewardMemberSimpleResponse(
	Long id, MemberSimpleResponse awardee, DomainType sourceType, RewardType rewardType
) {
	public static RewardMemberSimpleResponse from(RewardMember rewardMember) {
		return new RewardMemberSimpleResponse(
			rewardMember.getId(),
			MemberSimpleResponse.from(rewardMember.getAwardee()),
			rewardMember.getReward().getSourceType(),
			rewardMember.getReward().getRewardType()
		);
	}
}
