package kr.co.jeelee.kiwee.domain.questMember.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.questMember.dto.request.QuestMemberSuccessRequest;
import kr.co.jeelee.kiwee.domain.questMember.dto.request.QuestMemberPlannedRequest;
import kr.co.jeelee.kiwee.domain.questMember.dto.response.QuestMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.questMember.dto.response.QuestMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.questMember.model.QuestMemberStatus;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;

public interface QuestMemberService {

	QuestMemberDetailResponse plannedQuest(CustomOAuth2User principal, UUID questId, QuestMemberPlannedRequest request);
	QuestMemberDetailResponse successQuest(CustomOAuth2User principal, UUID questId, QuestMemberSuccessRequest request);

	QuestMemberDetailResponse getQuestMember(CustomOAuth2User principal, UUID questId);
	PagedResponse<QuestMemberSimpleResponse> getQuestMembers(CustomOAuth2User principal, QuestMemberStatus status, Pageable pageable);

	PagedResponse<QuestMemberSimpleResponse> findMembersByQuestId(CustomOAuth2User principal, UUID questId, Pageable pageable);

	QuestMemberDetailResponse updatePlannedQuest(CustomOAuth2User principal, UUID questId, UUID id, QuestMemberPlannedRequest request);

	void giveUpQuest(CustomOAuth2User principal, UUID questId);

	void autoSetStatus();

}
