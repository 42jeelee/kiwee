package kr.co.jeelee.kiwee.domain.authorization.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.authorization.dto.response.RoleResponse;
import kr.co.jeelee.kiwee.domain.authorization.entity.Role;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.model.RoleType;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

public interface RoleService {

	PagedResponse<RoleResponse> getRoles(DomainType domain, Pageable pageable);
	RoleResponse getById(UUID id);

	Role findByRoleType(RoleType roleType);

}
