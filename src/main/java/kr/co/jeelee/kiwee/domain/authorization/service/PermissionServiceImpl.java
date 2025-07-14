package kr.co.jeelee.kiwee.domain.authorization.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.authorization.dto.response.PermissionResponse;
import kr.co.jeelee.kiwee.domain.authorization.exception.PermissionNotFoundException;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.domain.authorization.repository.PermissionRepository;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PermissionServiceImpl implements PermissionService {

	private final PermissionRepository permissionRepository;

	@Override
	public PagedResponse<PermissionResponse> getDomainPermissions(DomainType domain, Pageable pageable) {
		return PagedResponse.of(permissionRepository.findByDomain(domain, pageable), PermissionResponse::from);
	}

	@Override
	public PermissionResponse getById(Long id) {
		return permissionRepository.findById(id)
			.map(PermissionResponse::from)
			.orElseThrow(PermissionNotFoundException::new);
	}
}
