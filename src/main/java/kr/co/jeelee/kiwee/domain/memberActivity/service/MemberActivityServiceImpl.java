package kr.co.jeelee.kiwee.domain.memberActivity.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.memberActivity.dto.response.MemberActivityResponse;
import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.domain.memberActivity.exception.MemberActivityNotFoundException;
import kr.co.jeelee.kiwee.domain.memberActivity.model.ActivityType;
import kr.co.jeelee.kiwee.domain.memberActivity.repository.MemberActivityRepository;
import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberActivityServiceImpl implements MemberActivityService {

	private final MemberActivityRepository memberActivityRepository;

	@Override
	public PagedResponse<MemberActivityResponse> memberActivities(UUID memberId, Pageable pageable) {
		return PagedResponse.of(
			memberActivityRepository.findByActorId(memberId, pageable),
			MemberActivityResponse::from
		);
	}

	@Override
	public UUID log(Member actor, ActivityType type, DomainType sourceType, UUID sourceId, String description) {
		return log(actor, type, sourceType, sourceId, description, new ArrayList<>());
	}

	@Override
	public UUID log(Member actor, ActivityType type, DomainType sourceType, UUID sourceId, String description, List<RewardMember> rewardMembers) {
		MemberActivity memberActivity = MemberActivity.of(actor, type, sourceType, sourceId, description, rewardMembers);
		return memberActivityRepository.save(memberActivity).getId();
	}

	@Override
	public void addRewards(UUID activityId, List<RewardMember> rewardMembers) {
		MemberActivity memberActivity = getById(activityId);

		memberActivity.addRewardMembers(rewardMembers);
		memberActivityRepository.save(memberActivity);
	}

	@Override
	public int getGeneralCount(UUID id) {
		MemberActivity activity = getById(id);

		return memberActivityRepository.countByActorIdAndSourceTypeAndType(
			activity.getActor().getId(),
			activity.getSourceType(),
			activity.getType()
		);
	}

	@Override
	public int getSpecificCount(UUID id) {
		MemberActivity activity = getById(id);

		return memberActivityRepository.countByActorIdAndSourceTypeAndSourceIdAndType(
			activity.getActor().getId(),
			activity.getSourceType(),
			activity.getSourceId(),
			activity.getType()
		);
	}

	@Override
	public MemberActivity getById(UUID id) {
		return memberActivityRepository.findById(id)
			.orElseThrow(MemberActivityNotFoundException::new);
	}
}
