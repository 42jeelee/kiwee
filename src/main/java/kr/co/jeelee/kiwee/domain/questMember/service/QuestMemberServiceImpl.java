package kr.co.jeelee.kiwee.domain.questMember.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.Reward.model.TriggerType;
import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.member.entity.Member;
import kr.co.jeelee.kiwee.domain.memberActivity.event.MemberActivityEvent;
import kr.co.jeelee.kiwee.domain.memberActivity.model.ActivityType;
import kr.co.jeelee.kiwee.domain.notification.event.NotificationEvent;
import kr.co.jeelee.kiwee.domain.notification.model.NotificationType;
import kr.co.jeelee.kiwee.domain.quest.entity.Quest;
import kr.co.jeelee.kiwee.domain.quest.service.QuestService;
import kr.co.jeelee.kiwee.domain.questMember.dto.request.QuestMemberSuccessRequest;
import kr.co.jeelee.kiwee.domain.questMember.dto.request.QuestMemberPlannedRequest;
import kr.co.jeelee.kiwee.domain.questMember.dto.response.QuestMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.questMember.dto.response.QuestMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.questMember.entity.QuestMember;
import kr.co.jeelee.kiwee.domain.questMember.exception.QuestCantInTimeException;
import kr.co.jeelee.kiwee.domain.questMember.exception.QuestMemberAlreadyExistException;
import kr.co.jeelee.kiwee.domain.questMember.exception.QuestMemberNotFoundException;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberStatus;
import kr.co.jeelee.kiwee.domain.questMember.repository.QuestMemberRepository;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.model.TermType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestMemberServiceImpl implements QuestMemberService {

	private final QuestMemberRepository questMemberRepository;

	private final QuestService questService;

	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public QuestMemberDetailResponse plannedQuest(CustomOAuth2User principal, UUID questId,
		QuestMemberPlannedRequest request) {
		Quest quest = questService.getById(questId);

		if (request.startDateTime().isAfter(request.endDateTime())) {
			throw new FieldValidationException("endDateTime", "종료 시간은 시작 시간 전일 수 없습니다.");
		}

		if (
			request.startDateTime().isBefore(
				getAvailableStartDateTime(quest.getRescheduleTerm())
			)
		) {
			throw new FieldValidationException("startDateTime", "시작 시간이 너무 일찍입니다.");
		}

		QuestMember questMember = QuestMember.of(
			quest,
			principal.member(),
			request.startDateTime(),
			request.endDateTime(),
			request.autoReschedule() != null? request.autoReschedule() : false
		);

		return saveQuestMemberAndTrigger(questMember, TriggerType.JOIN);
	}

	@Override
	@Transactional
	public QuestMemberDetailResponse successQuest(CustomOAuth2User principal, UUID questId,
		QuestMemberSuccessRequest request) {

		Quest quest = questService.getById(questId);

		if (!withinTimeRange(LocalTime.now(), quest.getVerifiableFrom(), quest.getVerifiableUntil())) {
			throw new QuestCantInTimeException();
		}

		QuestMember questMember = questMemberRepository.findByQuestAndMemberAndStatus(
			quest, principal.member(), QuestMemberStatus.PLANNED
		).map(qm -> {
			qm.complete(request.mediaType(), request.contentUrl(), request.message());
			return qm;
		})
		.orElseGet(() -> QuestMember.of(
			quest,
			principal.member(),
			QuestMemberStatus.SUCCEEDED,
			request.mediaType(),
			request.contentUrl(),
			request.message()
		));

		return saveQuestMemberAndTrigger(questMember, TriggerType.SUCCESS);
	}

	@Override
	public QuestMemberDetailResponse getQuestMember(CustomOAuth2User principal, UUID questId) {
		Quest quest = questService.getById(questId);

		return questMemberRepository.findByQuestAndMember(quest, principal.member())
			.map(QuestMemberDetailResponse::from)
			.orElseThrow(QuestMemberNotFoundException::new);
	}

	@Override
	public PagedResponse<QuestMemberSimpleResponse> getQuestMembers(
		CustomOAuth2User principal,
		QuestMemberStatus status,
		Pageable pageable
	) {
		return PagedResponse.of(
			questMemberRepository.findByMemberAndStatus(principal.member(), status, pageable),
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
	public QuestMemberDetailResponse updatePlannedQuest(CustomOAuth2User principal, UUID questId, UUID id,
		QuestMemberPlannedRequest request) {
		Quest quest = questService.getById(questId);

		QuestMember questMember = questMemberRepository.findById(id)
				.orElseThrow(QuestMemberNotFoundException::new);

		if (!questMember.getQuest().getId().equals(quest.getId())) {
			throw new QuestMemberNotFoundException("해당 퀘스트의 정보가 아닙니다.");
		}

		if (!questMember.getMember().getId().equals(principal.member().getId())) {
			throw new QuestMemberNotFoundException("다른 유저의 정보입니다.");
		}

		questMember.updatePeriod(request.startDateTime(), request.endDateTime());
		questMember.switchAutoReschedule(
			request.autoReschedule() != null? request.autoReschedule() : false
		);

		return QuestMemberDetailResponse.from(questMember);
	}

	@Override
	@Transactional
	public void giveUpQuest(CustomOAuth2User principal, UUID questId) {
		Quest quest = questService.getById(questId);
		QuestMember questMember = questMemberRepository.findByQuestAndMember(quest, principal.member())
			.orElseThrow(QuestMemberNotFoundException::new);

		questMember.failed();
		questMemberRepository.save(questMember);
	}

	@Override
	@Transactional
	@Scheduled(cron = "0 0 * * * *")
	public void autoSetStatus() {
		List<QuestMember> expiredQuestMember = questMemberRepository.findByStatusAndEndDateTimeBefore(
			QuestMemberStatus.IN_PROGRESS,
			LocalDateTime.now()
		);

		expiredQuestMember.forEach(questMember -> {
			questMember.failed();
			publishNotificationEvent(
				"퀘스트 실패 ..",
				String.format("'%s' 퀘스트를 기한 내에 성공하지 못하였습니다:(", questMember.getQuest().getTitle()),
				questMember
			);

			if (questMember.getAutoReschedule() != null && questMember.getAutoReschedule()) {
				rescheduleQuest(questMember);
			}
		});

		List<QuestMember> plannedQuestMember = questMemberRepository.findByStatus(QuestMemberStatus.PLANNED);

		plannedQuestMember.forEach(questMember -> {
			questMember.start();
			publishNotificationEvent(
				"퀘스트가 시작되었습니다 !!",
				String.format("'%s' 퀘스트가 시작되었습니다:)!", questMember.getQuest().getTitle()),
				questMember
			);
		});

	}

	private void publishNotificationEvent(String title, String message, QuestMember questMember) {
		eventPublisher.publishEvent(NotificationEvent.of(
			questMember.getMember().getId(),
			NotificationType.QUEST,
			questMember.getQuest().getId(),
			title,
			message,
			null
		));
	}

	private QuestMemberDetailResponse saveQuestMemberAndTrigger(QuestMember questMember, TriggerType triggerType) {
		Member member = questMember.getMember();
		Quest quest = questMember.getQuest();

		if (questMemberRepository.existsByQuestAndMemberAndStatus(quest, member, QuestMemberStatus.PLANNED)) {
			throw new QuestMemberAlreadyExistException();
		}

		QuestMember savedQuestMember = questMemberRepository.save(questMember);

		String action = triggerType == TriggerType.JOIN
			? "수락"
			: "성공";

		MemberActivityEvent activityEvent = MemberActivityEvent.of(
			member.getId(),
			DomainType.QUEST,
			quest.getId(),
			triggerType == TriggerType.JOIN ? ActivityType.JOIN : ActivityType.COMPLETE
		);

		eventPublisher.publishEvent(activityEvent);

		publishNotificationEvent(
			String.format("퀘스트 %s", action),
			String.format("'%s' 퀘스트를 %s 하였습니다.", quest.getTitle(), action),
			questMember
		);

		if (
			triggerType.equals(TriggerType.SUCCESS)
				&& questMember.getAutoReschedule() != null && questMember.getAutoReschedule()
		) {
			rescheduleQuest(questMember);
		}

		return QuestMemberDetailResponse.from(savedQuestMember);
	}

	private TemporalAmount getRescheduleDuration(TermType termType) {
		return switch (termType) {
			case NONE -> Duration.ZERO;
			case DAILY -> Duration.ofDays(1);
			case WEEKLY -> Duration.ofDays(7);
			case MONTHLY -> Period.ofMonths(1);
			case YEARLY -> Period.ofYears(1);
		};
	}

	private boolean withinTimeRange(LocalTime now, LocalTime start, LocalTime end) {
		if (start.isBefore(end)) {
			return !now.isBefore(start) && !now.isAfter(end);
		}

		return !now.isBefore(start) || !now.isAfter(end);
	}

	private void rescheduleQuest(QuestMember questMember) {
		TemporalAmount rescheduleDuration = getRescheduleDuration(questMember.getQuest().getRescheduleTerm());
		boolean isZero = questMember.getQuest().getRescheduleTerm().equals(TermType.NONE);
		LocalDateTime now = LocalDateTime.now();

		LocalDateTime startDateTime = isZero
			? now
			: questMember.getStartDateTime().plus(rescheduleDuration);

		LocalDateTime endDateTime = questMember.getEndDateTime() != null
			? isZero
				? now.plus(Duration.between(questMember.getStartDateTime(), questMember.getEndDateTime()))
				: questMember.getEndDateTime().plus(rescheduleDuration)
			: null;

		QuestMember newQuestMember = QuestMember.of(
			questMember.getQuest(),
			questMember.getMember(),
			startDateTime,
			endDateTime,
			true
		);

		saveQuestMemberAndTrigger(newQuestMember, TriggerType.JOIN);
	}

	private LocalDateTime getAvailableStartDateTime(TermType termType) {
		return switch (termType) {
			case NONE -> LocalDateTime.now();
			case DAILY -> LocalDate.now().plusDays(1).atStartOfDay();
			case WEEKLY -> LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atStartOfDay();
			case MONTHLY -> LocalDate.now().withDayOfMonth(1).plusMonths(1).atStartOfDay();
			case YEARLY -> LocalDate.of(LocalDate.now().getYear() + 1, 1, 1).atStartOfDay();
		};
	}

}
