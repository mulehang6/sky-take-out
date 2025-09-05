package com.sky.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger) 全局配置
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("外卖项目 API 文档")
                        .version("v1.0")
                        .description("这是一个基于 Spring Boot 的外卖项目 API 文档。"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                // 关键修改在这里！
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY) // 类型改为 APIKEY
                                        .in(SecurityScheme.In.HEADER)     // 位置在 Header
                                        .name("token")                    // Header 的名字叫 "token"
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}