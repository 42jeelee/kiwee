package kr.co.jeelee.kiwee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KiweeApplication {

	public static void main(String[] args) {
		SpringApplication.run(KiweeApplication.class, args);
	}

}
