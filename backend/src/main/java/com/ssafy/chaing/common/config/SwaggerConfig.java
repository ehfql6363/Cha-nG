package com.ssafy.chaing.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        servers = {
                @Server(url = "http://localhost:8080", description = "🖥️ 로컬 개발 서버"),
                @Server(url = "https://chaing.site", description = "🌐 운영 서버 (HTTPS)"),
                @Server(url = "http://chaing.site", description = "🌐 운영 서버 (HTTP, 리다이렉트용)")
                
        }
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT") // JWT 사용 시 추가
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .info(new Info()
                        .title("Spring Boot API Example")
                        .description("Spring Boot API 예시 프로젝트입니다.")
                        .version("v0.0.1")
                );
    }

}


