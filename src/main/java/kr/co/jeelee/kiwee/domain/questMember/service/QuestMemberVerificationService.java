package kr.co.jeelee.kiwee.domain.questMember.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.auth.oauth.user.CustomOAuth2User;
import kr.co.jeelee.kiwee.domain.questMember.dto.request.QuestMemberVerificationRequest;
import kr.co.jeelee.kiwee.domain.questMember.dto.response.QuestMemberVerificationResponse;
import kr.co.jeelee.kiwee.domain.questMember.entity.QuestMember;
import kr.co.jeelee.kiwee.domain.questMember.entity.QuestMemberVerification;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;

public interface QuestMemberVerificationService {

	QuestMemberVerificationResponse verify(CustomOAuth2User principal, UUID questId, QuestMemberVerificationRequest request);

	PagedResponse<QuestMemberVerificationResponse> getAllByMember(CustomOAuth2User principal, Pageable pageable);
	PagedResponse<QuestMemberVerificationResponse> getAllByQuest(UUID questId, Pageable pageable);

	List<QuestMemberVerification> getVerificationsByTerm(QuestMember questMember, LocalDate start, LocalDate end);

	QuestMemberVerification getById(Long id);

}
