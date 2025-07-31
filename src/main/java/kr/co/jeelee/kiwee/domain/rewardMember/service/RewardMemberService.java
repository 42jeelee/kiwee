package kr.co.jeelee.kiwee.domain.rewardMember.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.rewardMember.dto.response.RewardMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.rewardMember.dto.response.RewardMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface RewardMemberService {

	PagedResponse<RewardMemberSimpleResponse> getRewardsByMember(UUID memberId, Pageable pageable);

	RewardMemberDetailResponse getRewardMemberById(UUID memberId, Long id);

	LocalDate getDateByLastGetReward(UUID memberId, UUID rewardId);

	RewardMember createRewardMember(RewardMember rewardMember);

	void deleteRewardMemberById(Long id);

}
