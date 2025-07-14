package kr.co.jeelee.kiwee.domain.rewardMember.service;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.rewardMember.dto.response.RewardMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.rewardMember.dto.response.RewardMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface RewardMemberService {

	PagedResponse<RewardMemberSimpleResponse> getRewardsByMember(CustomOAuth2User principal, Pageable pageable);

	RewardMemberDetailResponse getRewardMemberById(CustomOAuth2User principal, Long id);

	RewardMember createRewardMember(RewardMember rewardMember);

	void deleteRewardMemberById(Long id);

}
