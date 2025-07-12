package kr.co.jeelee.kiwee.domain.rewardMember.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.BadgeMember.service.BadgeMemberService;
import kr.co.jeelee.kiwee.domain.Reward.model.RewardType;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.domain.member.service.MemberService;
import kr.co.jeelee.kiwee.domain.notification.event.NotificationEvent;
import kr.co.jeelee.kiwee.domain.notification.metadata.NotificationMetadata;
import kr.co.jeelee.kiwee.domain.notification.model.NotificationType;
import kr.co.jeelee.kiwee.domain.rewardMember.dto.response.RewardMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.rewardMember.dto.response.RewardMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.rewardMember.entity.RewardMember;
import kr.co.jeelee.kiwee.domain.rewardMember.exception.RewardMemberNotFoundException;
import kr.co.jeelee.kiwee.domain.rewardMember.repository.RewardMemberRepository;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.resolver.DomainObjectResolver;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RewardMemberServiceImpl implements RewardMemberService {

	private final RewardMemberRepository rewardMemberRepository;

	private final MemberService memberService;
	private final BadgeMemberService badgeMemberService;

	private final DomainObjectResolver domainObjectResolver;

	private final ApplicationEventPublisher eventPublisher;

	@Override
	public PagedResponse<RewardMemberSimpleResponse> getRewardsByMember(CustomOAuth2User principal, Pageable pageable) {
		return PagedResponse.of(
			rewardMemberRepository.findByAwardeeId(principal.member().getId(), pageable),
			RewardMemberSimpleResponse::from
		);
	}

	@Override
	public RewardMemberDetailResponse getRewardMemberById(CustomOAuth2User principal, Long id) {
		return rewardMemberRepository.findById(id)
			.map(rm -> {

				if (!rm.getAwardee().getId().equals(principal.member().getId())) {
					throw new AccessDeniedException("해당 맴버가 아닙니다");
				}

				return RewardMemberDetailResponse.from(rm, domainObjectResolver);
			})
			.orElseThrow(RewardMemberNotFoundException::new);
	}

	@Override
	@Transactional
	public RewardMember createRewardMember(RewardMember rewardMember) {
		RewardMember savedRewardMember = rewardMemberRepository.save(rewardMember);
		memberService.gainExp(rewardMember.getAwardee().getId(), savedRewardMember.getReward().getExp());

		List<NotificationMetadata.RelatedItem> notificationRelated = new ArrayList<>();

		if (rewardMember.getReward().getRewardType().equals(RewardType.BADGE)) {
			badgeMemberService.earnBadge(
				rewardMember.getAwardee().getId(),
				rewardMember.getReward().getRewardId()
			);

			notificationRelated.add(
				NotificationMetadata.RelatedItem.of(
					DomainType.BADGE,
					rewardMember.getReward().getRewardId()
				)
			);
		}

		NotificationEvent notificationEvent = NotificationEvent.of(
			savedRewardMember.getAwardee().getId(),
			NotificationType.REWARD,
			rewardMember.getReward().getId(),
			savedRewardMember.getReward().getTitle(),
			savedRewardMember.getReward().getDescription(),
			NotificationMetadata.of(notificationRelated)
		);

		eventPublisher.publishEvent(notificationEvent);

		return savedRewardMember;
	}

	@Override
	@Transactional
	public void deleteRewardMemberById(Long id) {
		RewardMember rewardMember = rewardMemberRepository.findById(id)
				.orElseThrow(RewardMemberNotFoundException::new);

		rewardMemberRepository.delete(rewardMember);
	}
}
