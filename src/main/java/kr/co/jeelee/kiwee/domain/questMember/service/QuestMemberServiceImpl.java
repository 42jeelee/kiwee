package kr.co.jeelee.kiwee.domain.questMember.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.Reward.event.RewardEvent;
import kr.co.jeelee.kiwee.domain.Reward.model.TriggerType;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.memberActivity.model.ActivityType;
import kr.co.jeelee.kiwee.domain.memberActivity.service.MemberActivityService;
import kr.co.jeelee.kiwee.domain.notification.event.NotificationEvent;
import kr.co.jeelee.kiwee.domain.notification.model.NotificationType;
import kr.co.jeelee.kiwee.domain.quest.entity.Quest;
import kr.co.jeelee.kiwee.domain.quest.service.QuestService;
import kr.co.jeelee.kiwee.domain.questMember.dto.request.QuestMemberCreateRequest;
import kr.co.jeelee.kiwee.domain.questMember.dto.response.QuestMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.questMember.dto.response.QuestMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.questMember.entity.QuestMember;
import kr.co.jeelee.kiwee.domain.questMember.entity.QuestMemberVerification;
import kr.co.jeelee.kiwee.domain.questMember.exception.QuestMemberNotFoundException;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberStatus;
import kr.co.jeelee.kiwee.domain.questMember.repository.QuestMemberRepository;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.exception.common.InvalidParameterException;
import kr.co.jeelee.kiwee.global.model.TermType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestMemberServiceImpl implements QuestMemberService {

	private final QuestMemberRepository questMemberRepository;

	private final QuestService questService;
	private final QuestMemberVerificationService questMemberVerificationService;
	private final MemberActivityService memberActivityService;

	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public QuestMemberDetailResponse joinQuest(CustomOAuth2User principal, UUID questId, QuestMemberCreateRequest request) {

		Quest quest = questService.getById(questId);
		if (
			request.startDate().isBefore(LocalDateTime.now()) ||
			(!quest.getIsThisInstant() && request.startDate().toLocalDate().isEqual(LocalDate.now()))
		) {
			throw new FieldValidationException("startDate", "startDate 필드가 잘못되었습니다.");
		}

		QuestMember questMember = QuestMember.of(
			quest,
			principal.member(),
			request.startDate(),
			request.verifiableFrom() != null ? request.verifiableFrom() : quest.getVerifiableFrom(),
			request.verifiableUntil() != null ? request.verifiableUntil() : quest.getVerifiableUntil()
		);

		QuestMember savedQuestMember = questMemberRepository.save(questMember);

		RewardEvent rewardEvent = RewardEvent.of(
			principal.member().getId(),
			DomainType.QUEST,
			quest.getId(),
			TriggerType.JOIN,
			1,
			memberActivityService.log(
				principal.member(),
				ActivityType.JOIN,
				DomainType.QUEST,
				quest.getId(),
				String.format("'%s' 퀘스트 수락", quest.getTitle())
			)
		);

		NotificationEvent notificationEvent = NotificationEvent.of(
			principal.member().getId(),
			NotificationType.QUEST,
			"퀘스트 수락",
			String.format("'%s' 퀘스트를 수락하였습니다.", quest.getTitle()),
			DomainType.QUEST,
			quest.getId()
		);

		eventPublisher.publishEvent(rewardEvent);
		eventPublisher.publishEvent(notificationEvent);

		return QuestMemberDetailResponse.from(savedQuestMember);
	}

	@Override
	public QuestMemberDetailResponse getQuestMember(CustomOAuth2User principal, UUID questId) {
		Quest quest = questService.getById(questId);

		return questMemberRepository.findByQuestAndMember(quest, principal.member())
			.map(QuestMemberDetailResponse::from)
			.orElseThrow(QuestMemberNotFoundException::new);
	}

	@Override
	public PagedResponse<QuestMemberSimpleResponse> getQuestMembers(CustomOAuth2User principal, Pageable pageable) {
		return PagedResponse.of(
			questMemberRepository.findByMember(principal.member(), pageable),
			QuestMemberSimpleResponse::from
		);
	}

	@Override
	public PagedResponse<QuestMemberSimpleResponse> findMembersByQuestId(
		CustomOAuth2User principal, UUID questId, Pageable pageable
	) {
		Quest quest = questService.getById(questId);

		return PagedResponse.of(
			questMemberRepository.findByQuestAndStatus(quest, QuestMemberStatus.IN_PROGRESS, pageable),
			QuestMemberSimpleResponse::from
		);
	}

	@Override
	@Transactional
	public void giveUpQuest(CustomOAuth2User principal, UUID questId) {
		Quest quest = questService.getById(questId);
		QuestMember questMember = questMemberRepository.findByQuestAndMember(quest, principal.member())
			.orElseThrow(QuestMemberNotFoundException::new);

		questMember.failure();
		questMemberRepository.save(questMember);
	}

	@Override
	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void autoFailOverdueQuest() {
		List<QuestMember> expiredQuestMember = questMemberRepository.findByStatusAndEndDateBefore(
			QuestMemberStatus.IN_PROGRESS,
			LocalDateTime.now()
		);

		expiredQuestMember.forEach(questMember -> {
			eventPublisher.publishEvent(getNotificationEvent(
				"퀘스트 실패 ..",
				String.format("'%s' 퀘스트를 기한 내에 성공하지 못하였습니다:(", questMember.getQuest().getTitle()),
				questMember
			));
			questMember.failure();
		});

	}

	@Override
	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void autoEvaluateProgressPerDay() {
		autoEvaluateProgressPerTerm(TermType.DAILY);
	}

	@Override
	@Transactional
	@Scheduled(cron = "0 0 0 * * Mon")
	public void autoEvaluateProgressPerWeekly() {
		autoEvaluateProgressPerTerm(TermType.WEEKLY);
	}

	@Override
	@Transactional
	@Scheduled(cron = "0 0 0 1 * *")
	public void autoEvaluateProgressPerMonthly() {
		autoEvaluateProgressPerTerm(TermType.MONTHLY);
	}

	@Override
	@Transactional
	@Scheduled(cron = "0 0 0 1 1 *")
	public void autoEvaluateProgressPerYearly() {
		autoEvaluateProgressPerTerm(TermType.YEARLY);
	}

	private void autoEvaluateProgressPerTerm(TermType termType) {
		LocalDate now = LocalDate.now();

		LocalDate startTerm = getStartDateOfTerm(termType, now);
		LocalDate endTerm = getEndDateOfTerm(termType, now);

		List<QuestMember> targets = questMemberRepository.findByStatusAndQuestTermType(
			QuestMemberStatus.IN_PROGRESS,
			termType
		);

		targets.forEach(questMember -> {
			if (questMember.getQuest().shouldSkipThisTerm()) return;

			List<QuestMemberVerification> verifications = questMemberVerificationService.getVerificationsByTerm(
				questMember,
				startTerm,
				endTerm
			);

			if (isTermFailed(termType, questMember.getQuest(), verifications)) {
				Integer failCount = questMember.addFailCount();
				String title = "퀘스트 조건 미달성";
				String message = String.format(
					"이번 '%s' 퀘스트 조건을 달성하지 못했어..",
					questMember.getQuest().getTitle()
				);

				if (failCount > questMember.getQuest().getMaxAllowedFails()) {
					questMember.failure();
					title = "퀘스트 실패 ..";
					message = String.format(
						"이번 '%s' 퀘스트 누적 %d회 달성하지 못해 실패했어 ..",
						questMember.getQuest().getTitle(),
						failCount
					);
				}
				questMemberRepository.save(questMember);

				eventPublisher.publishEvent(getNotificationEvent(
					title,
					message,
					questMember
				));
			}
		});

	}

	private NotificationEvent getNotificationEvent(String title, String message, QuestMember questMember) {
		return NotificationEvent.of(
			questMember.getMember().getId(),
			NotificationType.QUEST,
			title,
			message,
			DomainType.QUEST,
			questMember.getQuest().getId()
		);
	}

	private LocalDate getStartDateOfTerm(TermType termType, LocalDate now) {
		return switch (termType) {
			case DAILY -> now;
			case WEEKLY -> now.with(DayOfWeek.MONDAY);
			case MONTHLY -> now.withDayOfMonth(1);
			case YEARLY -> now.withDayOfYear(1);
			case NONE -> throw new InvalidParameterException("termType", "NONE 타입은 반복주기가 아닙니다.");
		};
	}

	private LocalDate getEndDateOfTerm(TermType termType, LocalDate now) {
		return switch (termType) {
			case DAILY -> now;
			case WEEKLY -> now.with(DayOfWeek.SUNDAY);
			case MONTHLY -> now.withDayOfMonth(now.lengthOfMonth());
			case YEARLY -> now.withDayOfYear(now.lengthOfYear());
			case NONE -> throw new InvalidParameterException("termType", "NONE 타입은 반복주기가 아닙니다.");
		};
	}

	private boolean isTermFailed(TermType termType, Quest quest, List<QuestMemberVerification> verifications) {
		List<Integer> activeDays = quest.getActiveDays();
		int minPerTerm = quest.getMinPerTerm() != null ? quest.getMinPerTerm() : 0;

		if (activeDays != null && !activeDays.isEmpty()) {
			switch (termType) {
				case DAILY -> {
					int day = LocalDate.now().minusDays(1).getDayOfWeek().getValue() % 7;
					if (!activeDays.contains(day)) {
						return false;
					}
				}
				case WEEKLY -> {
					for (int day : activeDays) {
						boolean hasVerificationOnDay = verifications.stream()
							.anyMatch(v -> {
								int d = v.getVerifiedDate().getDayOfWeek().getValue() % 7;
								return d == day;
							});

						if (!hasVerificationOnDay) return true;
					}
				}
				case MONTHLY -> {
					for (int day : activeDays) {
						boolean hasVerificationOnDay = verifications.stream()
							.anyMatch(v -> v.getVerifiedDate().getDayOfMonth() == day);

						if (!hasVerificationOnDay) return true;
					}
				}
			}
		}

		return verifications.size() < minPerTerm;
	}

}
