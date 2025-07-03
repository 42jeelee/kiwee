package kr.co.jeelee.kiwee.domain.questMember.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.Reward.event.RewardEvent;
import kr.co.jeelee.kiwee.domain.Reward.model.TriggerType;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.memberActivity.model.ActivityType;
import kr.co.jeelee.kiwee.domain.memberActivity.service.MemberActivityService;
import kr.co.jeelee.kiwee.domain.quest.entity.Quest;
import kr.co.jeelee.kiwee.domain.quest.service.QuestService;
import kr.co.jeelee.kiwee.domain.questMember.dto.request.QuestMemberVerificationRequest;
import kr.co.jeelee.kiwee.domain.questMember.dto.response.QuestMemberVerificationResponse;
import kr.co.jeelee.kiwee.domain.questMember.entity.QuestMember;
import kr.co.jeelee.kiwee.domain.questMember.entity.QuestMemberVerification;
import kr.co.jeelee.kiwee.domain.questMember.exception.QuestMemberNotFoundException;
import kr.co.jeelee.kiwee.domain.questMember.exception.QuestMemberVerificationNotFoundException;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberVerificationStatus;
import kr.co.jeelee.kiwee.domain.questMember.repository.QuestMemberRepository;
import kr.co.jeelee.kiwee.domain.questMember.repository.QuestMemberVerificationRepository;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.InvalidParameterException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestMemberVerificationServiceImpl implements QuestMemberVerificationService {

	private final QuestMemberRepository questMemberRepository;
	private final QuestMemberVerificationRepository questMemberVerificationRepository;

	private final QuestService questService;
	private final MemberActivityService memberActivityService;

	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public QuestMemberVerificationResponse verify(CustomOAuth2User principal, UUID questId, QuestMemberVerificationRequest request) {
		Quest quest = questService.getById(questId);
		QuestMember questMember = questMemberRepository.findByQuestAndMember(quest, principal.member())
			.orElseThrow(QuestMemberNotFoundException::new);

		if (request.contentUrl() != null && request.contentType() == null) {
			throw new InvalidParameterException("contentType", "contentUrl이 있을 경우 contentType은 비어 있을 수 없습니다.");
		}

		QuestMemberVerification questMemberVerification = QuestMemberVerification.of(
			questMember,
			LocalDate.now(),
			QuestMemberVerificationStatus.SUCCESS,
			request.contentType(),
			request.contentUrl(),
			request.message(),
			LocalDateTime.now()
		);
		QuestMemberVerification savedQuestMemberVerification =
			questMemberVerificationRepository.save(questMemberVerification);

		RewardEvent rewardEvent = RewardEvent.of(
			principal.member().getId(),
			DomainType.QUEST,
			quest.getId(),
			TriggerType.SUCCESS,
			questMemberVerificationRepository.countByQuestMember(questMember),
			memberActivityService.log(
				principal.member(),
				ActivityType.VERIFY,
				DomainType.QUEST,
				quest.getId(),
				String.format("'%s' 퀘스트 인증", quest.getTitle())
			)
		);

		eventPublisher.publishEvent(rewardEvent);

		return QuestMemberVerificationResponse.from(savedQuestMemberVerification);
	}

	@Override
	public PagedResponse<QuestMemberVerificationResponse> getAllByMember(CustomOAuth2User principal, Pageable pageable) {

		return PagedResponse.of(
			questMemberVerificationRepository.findByQuestMemberMemberId(principal.member().getId(), withCompletedAtSort(pageable)),
			QuestMemberVerificationResponse::from
		);
	}

	@Override
	public PagedResponse<QuestMemberVerificationResponse> getAllByQuest(UUID questId, Pageable pageable) {

		return PagedResponse.of(
			questMemberVerificationRepository.findByQuestMemberQuestId(questId, withCompletedAtSort(pageable)),
			QuestMemberVerificationResponse::from
		);
	}

	@Override
	public List<QuestMemberVerification> getVerificationsByTerm(QuestMember questMember, LocalDate start,
		LocalDate end) {

		if (start.isAfter(end)) {
			throw new InvalidParameterException("end", "start 보다 앞에 있을 수 없습니다.");
		}

		return questMemberVerificationRepository.findByQuestMemberAndStatusAndVerifiedDateBetween(
			questMember,
			QuestMemberVerificationStatus.SUCCESS,
			start,
			end
		);
	}

	@Override
	public QuestMemberVerification getById(Long id) {
		return questMemberVerificationRepository.findById(id)
			.orElseThrow(QuestMemberVerificationNotFoundException::new);
	}

	private Pageable withCompletedAtSort(Pageable pageable) {
		Sort completedSort = pageable.getSort().and(Sort.by(Sort.Direction.DESC, "completedAt"));
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), completedSort);
	}
}
