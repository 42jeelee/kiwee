package kr.co.jeelee.kiwee.domain.reputations.service;

import java.time.YearMonth;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.reputations.dto.request.ReputationsCreateRequest;
import kr.co.jeelee.kiwee.domain.reputations.dto.response.ReputationsStatResponse;
import kr.co.jeelee.kiwee.domain.reputations.dto.response.ReputationsVoteResponse;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface ReputationService {

	void vote(ReputationsCreateRequest reputationsCreateRequest);

	PagedResponse<ReputationsStatResponse> getLank(YearMonth yearMonth, Pageable pageable);
	ReputationsStatResponse getStatByMemberId(UUID id, YearMonth yearMonth);

	PagedResponse<ReputationsVoteResponse> getVotesByMemberId(UUID id, Pageable pageable);

}
