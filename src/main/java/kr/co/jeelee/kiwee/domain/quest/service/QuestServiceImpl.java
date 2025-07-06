package kr.co.jeelee.kiwee.domain.quest.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.authorization.model.PermissionType;
import kr.co.jeelee.kiwee.domain.channel.entity.Channel;
import kr.co.jeelee.kiwee.domain.channel.service.ChannelService;
import kr.co.jeelee.kiwee.domain.channelMember.service.ChannelMemberService;
import kr.co.jeelee.kiwee.domain.quest.dto.request.QuestCreateRequest;
import kr.co.jeelee.kiwee.domain.quest.dto.request.QuestUpdateRequest;
import kr.co.jeelee.kiwee.domain.quest.dto.response.QuestDetailResponse;
import kr.co.jeelee.kiwee.domain.quest.dto.response.QuestSimpleResponse;
import kr.co.jeelee.kiwee.domain.quest.entity.Quest;
import kr.co.jeelee.kiwee.domain.quest.exception.QuestNotFoundException;
import kr.co.jeelee.kiwee.domain.quest.repository.QuestRepository;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;
import kr.co.jeelee.kiwee.global.exception.common.AccessDeniedException;
import kr.co.jeelee.kiwee.global.exception.common.FieldValidationException;
import kr.co.jeelee.kiwee.global.model.TermType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestServiceImpl implements QuestService {

	private final QuestRepository questRepository;

	private final ChannelService channelService;
	private final ChannelMemberService channelMemberService;

	@Override
	@Transactional
	public QuestDetailResponse createQuest(CustomOAuth2User principal, QuestCreateRequest request) {
		Channel channel = channelService.getById(request.channelId());

		if (!channelMemberService.hasPermission(
			channel,
			principal.member(),
			PermissionType.ROLE_CHANNEL_CREATE_QUEST
		)) {
			throw new AccessDeniedException("채널 퀘스트를 만들 권한이 없습니다.");
		}

		Quest quest = Quest.of(
			request.icon(),
			request.banner(),
			request.title(),
			request.description(),
			channel,
			principal.member(),
			request.verifiableFrom(),
			request.verifiableUntil(),
			request.isThisInstant(),
			request.isRepeatable(),
			request.maxSuccess() == null ? 0 : request.maxSuccess(),
			request.maxRetryAllowed() == null ? 0 : request.maxRetryAllowed(),
			true,
			request.autoReschedule(),
			request.rescheduleTerm()
		);

		return QuestDetailResponse.from(questRepository.save(quest));
	}

	@Override
	public PagedResponse<QuestSimpleResponse> getAllQuestsByChannel(
		CustomOAuth2User principal, UUID channelId, Pageable pageable
	) {
		Channel channel = channelService.getById(channelId);

		if (!channelMemberService.isJoined(channel, principal.member())) {
			throw new AccessDeniedException("해당 채널에 가입되어 있지 않습니다.");
		}

		return PagedResponse.of(
			questRepository.findByChannelId(channelId, pageable),
			QuestSimpleResponse::from
		);
	}

	@Override
	public QuestDetailResponse getQuestDetail(UUID id) {
		return questRepository.findById(id)
			.map(QuestDetailResponse::from)
			.orElseThrow(QuestNotFoundException::new);
	}

	@Override
	@Transactional
	public QuestDetailResponse updateQuest(CustomOAuth2User principal, UUID id, QuestUpdateRequest request) {
		Quest quest = getById(id);

		if (!quest.getProposer().equals(principal.member())) {
			throw new AccessDeniedException("퀘스트 제안자가 아닙니다.");
		}

		updateIconIfChanged(quest, request.icon());
		updateBannerIfChanged(quest, request.banner());
		updateTitleIfChanged(quest, request.title());
		updateDescriptionIfChanged(quest, request.description());
		quest.updateVerifiableTerm(request.verifiableFrom(), request.verifiableUntil());
		updateIsThisInstantIfChanged(quest, request.isThisInstant());
		updateIsRepeatableIfChanged(quest, request.isRepeatable());
		quest.updateMaxSuccess(quest.getMaxSuccess());
		quest.updateMaxRetryAllowed(quest.getMaxRetryAllowed());
		updateIsActiveIfChanged(quest, request.isActive());
		updateAutoRescheduleIfChanged(quest, request.autoReschedule(), request.rescheduleTerm());

		return QuestDetailResponse.from(quest);
	}

	@Override
	@Transactional
	public void switchActivate(UUID id, Boolean isActive) {
		Quest quest = getById(id);

		quest.isActive(isActive);
		questRepository.save(quest);
	}

	@Override
	@Transactional
	public void deleteQuest(CustomOAuth2User principal, UUID id) {
		Quest quest = getById(id);

		if (!quest.getProposer().equals(principal.member())) {
			throw new AccessDeniedException("퀘스트 제안자가 아닙니다.");
		}

		questRepository.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteQuestForce(UUID id) {
		questRepository.deleteById(id);
	}

	@Override
	public Quest getById(UUID id) {
		return questRepository.findById(id)
			.orElseThrow(QuestNotFoundException::new);
	}

	private void updateIconIfChanged(Quest quest, String icon) {
		if (icon != null && !icon.equals(quest.getIcon())) {
			quest.updateIcon(icon);
		}
	}

	private void updateBannerIfChanged(Quest quest, String banner) {
		if (banner != null && !banner.equals(quest.getBanner())) {
			quest.updateBanner(banner);
		}
	}

	private void updateTitleIfChanged(Quest quest, String title) {
		if (title != null && !title.equals(quest.getTitle())) {
			quest.updateTitle(title);
		}
	}

	private void updateDescriptionIfChanged(Quest quest, String description) {
		if (description != null && !description.equals(quest.getDescription())) {
			quest.updateDescription(description);
		}
	}

	private void updateIsThisInstantIfChanged(Quest quest, Boolean isThisInstant) {
		if (isThisInstant != null && !isThisInstant.equals(quest.getIsThisInstant())) {
			quest.isThisInstant(isThisInstant);
		}
	}

	private void updateIsRepeatableIfChanged(Quest quest, Boolean isRepeatable) {
		if (isRepeatable != null && !isRepeatable.equals(quest.getIsRepeatable())) {
			quest.isRepeatable(isRepeatable);
		}
	}

	private void updateIsActiveIfChanged(Quest quest, Boolean isActive) {
		if (isActive != null && !isActive.equals(quest.getIsActive())) {
			quest.isActive(isActive);
		}
	}

	private void updateAutoRescheduleIfChanged(Quest quest, Boolean isAutoReschedule, TermType rescheduleTerm) {
		if (isAutoReschedule != null) {
			if (isAutoReschedule && rescheduleTerm != null) {
				quest.isAutoReschedule(rescheduleTerm);
				return;
			}
			if (!isAutoReschedule) {
				quest.isNotAutoReschedule();
				return;
			}

			throw new FieldValidationException("rescheduleTerm", "autoReschedule을 등록하기 위해서는 있어야 합니다.");
		}
	}

}
