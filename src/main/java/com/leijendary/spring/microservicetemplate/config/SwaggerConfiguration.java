package com.leijendary.spring.microservicetemplate.config;

import com.leijendary.spring.microservicetemplate.data.AppPageable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static springfox.documentation.spi.DocumentationType.OAS_30;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(OAS_30)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(Pageable.class, AppPageable.class)
                .genericModelSubstitutes(CompletableFuture.class, ResponseEntity.class)
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(apiKey()));
    }

    public SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        final var authorizationScope = new AuthorizationScope("global", "accessEverything");
        final var authorizationScopes = new AuthorizationScope[] { authorizationScope };

        return List.of(new SecurityReference("JWT", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }
}
