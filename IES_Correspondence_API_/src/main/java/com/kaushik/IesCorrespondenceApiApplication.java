package com.kaushik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IesCorrespondenceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IesCorrespondenceApiApplication.class, args);
	}

}
