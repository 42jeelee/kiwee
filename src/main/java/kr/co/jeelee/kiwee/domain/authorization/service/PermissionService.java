package kr.co.jeelee.kiwee.domain.authorization.service;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.authorization.dto.response.PermissionResponse;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface PermissionService {

	PagedResponse<PermissionResponse> getDomainPermissions(DomainType domain, Pageable pageable);
	PermissionResponse getById(Long id);

}
