package kr.co.jeelee.kiwee.domain.pledgeMember.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.pledgeMember.dto.request.PledgeMemberCreateRequest;
import kr.co.jeelee.kiwee.domain.pledgeMember.dto.response.PledgeMemberDetailResponse;
import kr.co.jeelee.kiwee.domain.pledgeMember.dto.response.PledgeMemberSimpleResponse;
import kr.co.jeelee.kiwee.domain.pledgeMember.entity.PledgeMember;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface PledgeMemberService {

	PledgeMemberDetailResponse joinPledge(UUID memberId, UUID pledgeId, PledgeMemberCreateRequest request);

	PledgeMemberDetailResponse getPledgeMember(UUID id);

	PagedResponse<PledgeMemberSimpleResponse> getMemberPledges(UUID memberId, Pageable pageable);

	PagedResponse<PledgeMemberSimpleResponse> getPledgeMembers(UUID pledgeId, Pageable pageable);

	PledgeMemberSimpleResponse delayPledge(UUID id, LocalDateTime startAt);

	PledgeMemberSimpleResponse giveUpPledge(UUID id);

	void calculateProgress(PledgeMember pledgeMember);
	void calculateProgress(PledgeMember pledgeMember, int prev);

	PledgeMember getById(UUID id);

}
