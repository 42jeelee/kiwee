package kr.co.jeelee.kiwee.domain.authorization.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import kr.co.jeelee.kiwee.domain.authorization.dto.response.RoleResponse;
import kr.co.jeelee.kiwee.domain.authorization.entity.Role;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;
import kr.co.jeelee.kiwee.domain.authorization.model.RoleType;
import kr.co.jeelee.kiwee.global.dto.response.PagedResponse;

public interface RoleService {

	PagedResponse<RoleResponse> getRoles(DomainType domain, Pageable pageable);
	RoleResponse getById(UUID id);

	Role findByRoleType(RoleType roleType);

}
