package com.ssafy.videoconference;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SsafyCommonProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsafyCommonProjectApplication.class, args);
	}

}
