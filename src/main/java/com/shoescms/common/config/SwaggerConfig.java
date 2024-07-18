package com.shoescms.common.config;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI swaggerApi() {
        Server server = new Server()
                .url("/")
                .description("Default Server url");

        Info info = new Info()
                .version("v1.0.0")
                .title("photoism_CMS API Documentation")
                .description("photoism_CMS API")
                .license(new License().name("seobuk").url("http://seobuk.kr"));

        Components components = new Components().addSecuritySchemes("x-api-token", new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("x-api-token"));

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("x-api-token");

        SpringDocUtils.getConfig().replaceWithClass(Pageable.class, Page.class);

        return new OpenAPI()
                .addServersItem(server)
                .info(info)
                .components(components)
                .security(List.of(securityRequirement));
    }

    @Getter
    @Setter
    @Schema
    static class Page {
        @Schema(description = "페이지 번호(0..N)")
        private Integer page;

        @Schema(description = "페이지 크기")
        private Integer size;

        @Schema(description = "정렬(사용법: 컬럼명, asc|desc)")
        private List<String> sort;
    }
}
