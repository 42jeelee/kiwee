package kr.co.jeelee.kiwee.domain.pledge.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.pledge.dto.request.PledgeCreateRequest;
import kr.co.jeelee.kiwee.domain.pledge.dto.response.PledgeDetailResponse;
import kr.co.jeelee.kiwee.domain.pledge.dto.response.PledgeSimpleResponse;
import kr.co.jeelee.kiwee.domain.pledge.entity.Pledge;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import kr.co.jeelee.kiwee.global.model.TermType;

public interface PledgeService {

	PledgeDetailResponse createPledge(UUID proposerId, PledgeCreateRequest request);

	PledgeDetailResponse getPledgeDetail(UUID id);

	PagedResponse<PledgeSimpleResponse> getPledges(UUID proposerId, TermType termType, Pageable pageable);

	void deletePledge(UUID proposerId, UUID id);

	Pledge getById(UUID id);

}
