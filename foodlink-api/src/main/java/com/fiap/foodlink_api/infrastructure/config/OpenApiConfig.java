package com.fiap.foodlink_api.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI foodlinkOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Foodlink API")
						.description("API para gestao de usuarios, restaurantes e itens de cardapio.")
						.version("v1"));
	}
}
