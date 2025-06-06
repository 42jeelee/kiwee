package kr.co.jeelee.kiwee.global.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.authorization.entity.Permission;
import kr.co.jeelee.kiwee.domain.authorization.entity.Role;
import kr.co.jeelee.kiwee.domain.authorization.model.PermissionType;
import kr.co.jeelee.kiwee.domain.authorization.model.RoleType;
import kr.co.jeelee.kiwee.domain.authorization.repository.PermissionRepository;
import kr.co.jeelee.kiwee.domain.authorization.repository.RoleRepository;
import lombok.RequiredArgsConstructor;

@Order(2)
@Component
@RequiredArgsConstructor
public class RoleInitializer implements ApplicationRunner {

	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;

	@Override
	@Transactional
	public void run(ApplicationArguments args) throws Exception {
		for (RoleType roleType : RoleType.values()) {
			Role role = roleRepository.findByName(roleType.name())
				.orElseGet(() -> roleRepository.save(
					Role.of(
						roleType.name(),
						roleType.getDomain(),
						roleType.getColor(),
						roleType.getDescription()
					)
				));

			for (PermissionType permissionType : roleType.getPermissions()) {
				Permission permission = permissionRepository.findByName(permissionType)
					.orElseGet(() -> permissionRepository.save(Permission.of(
						permissionType.getDomainType(),
						permissionType,
						permissionType.getDescription()
						)
					));

				role.addPermission(permission);
			}
			roleRepository.save(role);
		}
	}
}
