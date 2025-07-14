package kr.co.jeelee.kiwee.domain.authorization.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.jeelee.kiwee.domain.authorization.dto.response.RoleResponse;
import kr.co.jeelee.kiwee.global.model.DomainType;
import kr.co.jeelee.kiwee.domain.authorization.service.RoleService;
import kr.co.jeelee.kiwee.global.dto.response.common.PagedResponse;
import lombok.RequiredArgsConstructor;

@PreAuthorize(value = "hasRole('PERMISSION_GRANTER')")
@RestController
@RequestMapping(value = "/api/v1/roles")
@RequiredArgsConstructor
@Validated
public class RolesController {

	private final RoleService roleService;

	@GetMapping
	public PagedResponse<RoleResponse> getRoles(
		@RequestParam(defaultValue = "globals") DomainType domainType,
		@PageableDefault Pageable pageable
	) {
		return roleService.getRoles(domainType, pageable);
	}

	@GetMapping(value = "/{id}")
	public RoleResponse getById(
		@PathVariable UUID id
	) {
		return roleService.getById(id);
	}

}
