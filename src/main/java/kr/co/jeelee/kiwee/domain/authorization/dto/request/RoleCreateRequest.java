package kr.co.jeelee.kiwee.domain.authorization.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import kr.co.jeelee.kiwee.domain.authorization.model.DomainType;

public record RoleCreateRequest(
	@NotBlank(message = "name can't be Blank.") String name,
	@NotBlank(message = "domain can't be Blank.") DomainType domain,
	String color,
	String description,
	List<Long> permissionId
) {
}
