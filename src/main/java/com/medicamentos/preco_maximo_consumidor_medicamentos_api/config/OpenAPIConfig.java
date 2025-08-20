package com.medicamentos.preco_maximo_consumidor_medicamentos_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Preço Máximo ao Consumidor (PMC) de Medicamentos")
                        .version("v1.0")
                        .description("API RESTful para consulta do Preço Máximo ao Consumidor (PMC) de medicamentos, regulamentado pela CMED (Câmara de Regulação do Mercado de Medicamentos).")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}