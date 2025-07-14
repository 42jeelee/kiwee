package kr.co.jeelee.kiwee.domain.authorization.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.authorization.dto.response.RoleResponse;
import kr.co.jeelee.kiwee.domain.authorization.entity.Role;
import kr.co.jeelee.kiwee.domain.authorization.exception.RoleNotFoundException;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.global.model.RoleType;
import kr.co.jeelee.kiwee.domain.authorization.repository.RoleRepository;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	@Override
	public PagedResponse<RoleResponse> getRoles(DomainType domain, Pageable pageable) {
		return PagedResponse.of(roleRepository.findByDomain(domain, pageable), RoleResponse::from);
	}

	@Override
	public RoleResponse getById(UUID id) {
		return roleRepository.findById(id)
			.map(RoleResponse::from)
			.orElseThrow(RoleNotFoundException::new);
	}

	@Override
	public Role findByRoleType(RoleType roleType) {
		return roleRepository.findByName(roleType)
			.orElseThrow(RoleNotFoundException::new);
	}
}
