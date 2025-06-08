package com.kaushik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
public class SsaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsaApiApplication.class, args);
	}

}
