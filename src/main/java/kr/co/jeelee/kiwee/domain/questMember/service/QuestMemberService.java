package kr.co.jeelee.kiwee.domain.questMember.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.questMember.dto.request.QuestMemberCreateRequest;
import kr.co.jeelee.kiwee.domain.questMember.dto.response.QuestMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.questMember.dto.response.QuestMemberSimpleResponse;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;

public interface QuestMemberService {

	QuestMemberDetailResponse joinQuest(CustomOAuth2User principal, UUID questId, QuestMemberCreateRequest request);

	QuestMemberDetailResponse getQuestMember(CustomOAuth2User principal, UUID questId);
	PagedResponse<QuestMemberSimpleResponse> getQuestMembers(CustomOAuth2User principal, Pageable pageable);

	PagedResponse<QuestMemberSimpleResponse> findMembersByQuestId(CustomOAuth2User principal, UUID questId, Pageable pageable);

	void giveUpQuest(CustomOAuth2User principal, UUID questId);

	void autoFailOverdueQuest();

	void autoEvaluateProgressPerDay();
	void autoEvaluateProgressPerWeekly();
	void autoEvaluateProgressPerMonthly();
	void autoEvaluateProgressPerYearly();

}
