package kr.co.jeelee.kiwee.domain.rewardMember.dto.response;

import kr.co.jeelee.kiwee.domain.reward.dto.response.RewardSimpleResponse;
import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.memberActivity.dto.response.MemberActivityResponse;
import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;

public record RewardMemberDetailResponse(
	Long id, MemberSimpleResponse awardee, RewardSimpleResponse reward, MemberActivityResponse activity
) {
	public static RewardMemberDetailResponse from(
		RewardMember rewardMember, DomainObjectResolver resolver
	) {
		return new RewardMemberDetailResponse(
			rewardMember.getId(),
			MemberSimpleResponse.from(rewardMember.getAwardee()),
			RewardSimpleResponse.from(rewardMember.getReward(), resolver),
			MemberActivityResponse.from(rewardMember.getActivity())
		);
	}
}
