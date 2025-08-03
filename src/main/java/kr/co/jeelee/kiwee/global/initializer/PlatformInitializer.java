package kr.co.jeelee.kiwee.global.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import kr.co.jeelee.kiwee.domain.platform.repository.PlatformRepository;
import kr.co.jeelee.kiwee.global.model.PlatformInfo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlatformInitializer implements ApplicationRunner {

	private final PlatformRepository platformRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		for (PlatformInfo platformInfo : PlatformInfo.values()) {
			if (!platformRepository.existsByName(platformInfo.getName())) {
				platformRepository.save(platformInfo.toEntity());
			}
		}
	}
}
