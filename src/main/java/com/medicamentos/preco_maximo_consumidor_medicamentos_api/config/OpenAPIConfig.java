package com.medicamentos.preco_maximo_consumidor_medicamentos_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    private static final String SECURITY_SCHEME_NAME = "keycloakAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Preço Máximo ao Consumidor (PMC) de Medicamentos")
                        .version("v1.0")
                        .description("API RESTful para consulta do Preço Máximo ao Consumidor (PMC) de medicamentos, regulamentado pela CMED (Câmara de Regulação do Mercado de Medicamentos).")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))

                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))

                // Define os componentes de segurança, incluindo o esquema OAuth2
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows()
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl("http://localhost:8888/realms/preco-maximo-consumidor-medicamentos-realm/protocol/openid-connect/auth")
                                                .tokenUrl("http://localhost:8888/realms/preco-maximo-consumidor-medicamentos-realm/protocol/openid-connect/token")
                                                .scopes(new io.swagger.v3.oas.models.security.Scopes())
                                        )
                                )
                        )
                );
    }
}