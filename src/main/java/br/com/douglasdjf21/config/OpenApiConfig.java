package br.com.douglasdjf21.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
	
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Rest OpenAPI com Spring Boot 3")
						.version("v1")
						.description("Java 18 com Spring Boot 3")
						.termsOfService("https://termos.servico.com.br")
						.license(new License()
									.name("Apache 2")
									.url("https://licente.servico.com.br")));
	}

}
