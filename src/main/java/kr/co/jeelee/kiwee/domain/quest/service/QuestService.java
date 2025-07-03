package kr.co.jeelee.kiwee.domain.quest.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.quest.dto.request.QuestCreateRequest;
import kr.co.jeelee.kiwee.domain.quest.dto.request.QuestUpdateRequest;
import kr.co.jeelee.kiwee.domain.quest.dto.response.QuestDetailResponse;
import kr.co.jeelee.kiwee.domain.quest.dto.response.QuestSimpleResponse;
import kr.co.jeelee.kiwee.domain.quest.entity.Quest;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;

public interface QuestService {

	QuestDetailResponse createQuest(CustomOAuth2User principal, QuestCreateRequest request);

	PagedResponse<QuestSimpleResponse> getAllQuestsByChannel(CustomOAuth2User principal, UUID channelId, Pageable pageable);

	QuestDetailResponse getQuestDetail(UUID id);

	QuestDetailResponse updateQuest(CustomOAuth2User principal, UUID id, QuestUpdateRequest request);

	void switchActivate(UUID id, Boolean isActive);

	void deleteQuest(CustomOAuth2User principal, UUID id);
	void deleteQuestForce(UUID id);

	Quest getById(UUID id);

}
