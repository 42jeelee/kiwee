package kr.co.jeelee.kiwee.global.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.jeelee.kiwee.domain.authorization.entity.Permission;
import kr.co.jeelee.kiwee.global.model.PermissionType;
import kr.co.jeelee.kiwee.domain.authorization.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PermissionInitializer implements ApplicationRunner {

	private final PermissionRepository permissionRepository;

	@Override
	@Transactional
	public void run(ApplicationArguments args) throws Exception {
		for (PermissionType permissionType : PermissionType.values()) {
			permissionRepository.findByName(permissionType)
				.orElseGet(() -> permissionRepository.save(
						Permission.of(
							permissionType.getDomainType(),
							permissionType,
							permissionType.getDescription()
						)
					)
				);
		}
	}

}
