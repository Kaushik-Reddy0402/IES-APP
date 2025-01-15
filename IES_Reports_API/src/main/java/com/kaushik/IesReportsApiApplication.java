package com.kaushik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IesReportsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IesReportsApiApplication.class, args);
	}

}
