package kr.co.jeelee.kiwee.domain.memberActivity.dto.response;

import java.util.List;
import java.util.UUID;

import kr.co.jeelee.kiwee.domain.member.dto.response.MemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.domain.memberActivity.model.ActivityType;
import kr.co.jeelee.kiwee.domain.rewardMember.dto.response.RewardMemberSimpleResponse;

public record MemberActivityResponse(
	UUID id, MemberSimpleResponse actor, ActivityType type, UUID sourceId,
	String description, List<RewardMemberSimpleResponse> reward
) {
	public static MemberActivityResponse from(MemberActivity memberActivity) {
		return new MemberActivityResponse(
			memberActivity.getId(),
			MemberSimpleResponse.from(memberActivity.getActor()),
			memberActivity.getType(),
			memberActivity.getSourceId(),
			memberActivity.getDescription(),
			memberActivity.getRewardMembers().stream()
				.map(RewardMemberSimpleResponse::from)
				.toList()
		);
	}
}
