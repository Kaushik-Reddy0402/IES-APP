package com.kaushik.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

	@Bean
	OpenAPI configOpenAPI() {
		return new OpenAPI()
	    .info(new Info()
	        .title("Correspondence API")
	        .version("2.2.7")
	        .license(new License()
	            .name("Apache 2.0")
	            .url("http://www.apache.org/licenses/LICENSE-2.0")));
	}
}
