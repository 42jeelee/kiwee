package kr.co.jeelee.kiwee.domain.memberActivity.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.content.model.ContentType;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.memberActivity.dto.response.MemberActivityResponse;
import kr.co.jeelee.kiwee.domain.memberActivity.entity.MemberActivity;
import kr.co.jeelee.kiwee.global.model.ActivityType;
import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.model.TermType;
import kr.co.jeelee.kiwee.global.vo.ActivityCriterion;

public interface MemberActivityService {

	PagedResponse<MemberActivityResponse> memberActivities(UUID memberId, Pageable pageable);

	UUID log(Member actor, ActivityType type, DomainType sourceType, UUID sourceId, String description);

	UUID log(Member actor, ActivityType type, DomainType sourceType, UUID sourceId, String description, List<RewardMember> rewardMembers);

	void addRewards(UUID activityId, List<RewardMember> rewardMembers);

	List<MemberActivity> getTimeMemberActivities(UUID actorId, LocalDateTime start, LocalDateTime end);

	List<MemberActivity> getTimeAllActivities(LocalDateTime start, LocalDateTime end);

	Duration getPlayDuration(MemberActivity endActivity);

	Duration getPlayDurationByTerm(UUID actorId, DomainType sourceType, UUID sourceId, ActivityType activityType, LocalDateTime start, LocalDateTime end);

	boolean existsActivityByCriterionAtTime(UUID actorId, ActivityCriterion criterion, LocalDateTime start, LocalDateTime end);

	int countChildrenByContentId(UUID actorId, UUID contentId, ActivityType activityType);

	int countChildrenByContentIdAndTerm(UUID actorId, UUID contentId, ActivityType activityType, LocalDateTime start, LocalDateTime end);

	int countCriterionAtTime(UUID actorId, ActivityCriterion criterion, LocalDateTime start, LocalDateTime end);

	int countConsecutiveCount(UUID actorId, ActivityCriterion criterion, TermType termType, int num);

	int countConsecutiveCount(UUID actorId, ActivityCriterion criterion, TermType termType, ContentType contentType, int num);

	int countByCriterion(UUID actorId, ActivityCriterion criterion);

	int getGeneralContentTypeCount(UUID id, ContentType contentType);

	int getGeneralCount(UUID id);

	int getSpecificCount(UUID id);

	int getGeneralContentTypeCountByTerm(UUID id, ContentType contentType, LocalDateTime start, LocalDateTime end);

	MemberActivity getById(UUID id);

}
