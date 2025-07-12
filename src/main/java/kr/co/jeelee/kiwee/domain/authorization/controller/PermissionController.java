package kr.co.jeelee.kiwee.domain.authorization.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.jeelee.kiwee.domain.authorization.dto.response.PermissionResponse;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.domain.authorization.service.PermissionService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@PreAuthorize(value = "hasRole('PERMISSION_GRANTER')")
@RestController
@RequestMapping(value = "/api/v1/permissions")
@RequiredArgsConstructor
@Validated
public class PermissionController {

	private final PermissionService permissionService;

	@GetMapping
	public PagedResponse<PermissionResponse> getDomainPermissions(
		@RequestParam(defaultValue = "globals") DomainType domainType,
		@PageableDefault Pageable pageable
	) {
		return permissionService.getDomainPermissions(domainType, pageable);
	}

	@GetMapping(value = "/{id}")
	public PermissionResponse getById(
		@PathVariable Long id
	) {
		return permissionService.getById(id);
	}

}
