package com.apu.example.springsecurityjwt.config;

/*import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContext;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;*/

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

@EnableSwagger2
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    @Value("${host.full.dns.auth.link}")
    private String authLink;
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
//                .securitySchemes(Arrays.asList(securityScheme()))
                .securitySchemes(Arrays.asList(apiKeys()))
                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.apu.example.springsecurityjwt"))
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
//                .directModelSubstitute(Pageable.class, SwaggerPageable.class)
//                .directModelSubstitute(Sort.class, String[].class);
    }
    private SecurityScheme securityScheme() {
        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpoint(authLink + "/api/auth/authenticate", "accessToken"))
                .tokenRequestEndpoint(
                        new TokenRequestEndpoint(authLink + "/api/auth/authenticate", "admin@gmail.com", "1234"))
                .build();

        SecurityScheme oauth = new OAuthBuilder().name("spring_security")
                .grantTypes(Arrays.asList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
        return oauth;
    }
    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
                new AuthorizationScope("read", "for read operations"),
                new AuthorizationScope("write", "for write operations")};
        return scopes;
    }



    private ApiKey apiKeys(){
//        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
        return new ApiKey("accessToken", AUTHORIZATION_HEADER, "header");

    }
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }
    /*private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }*/
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Jwt Spring Security Swagger").description("")
                .termsOfServiceUrl("")
                .contact(new Contact("Apu KUmar Chakroborti", "", ""))
                .license("Open Source").licenseUrl("").version("1.0.0").build();
    }
    /*private ApiInfo apiInfo() {
        return new ApiInfo("My REST API",
                "Some custom description of API.",
                "1.0",
                "Terms of service",
                new Contact("Sallo Szrajbman", "www.baeldung.com", "salloszraj@gmail.com"),
                "License of API",
                "API license URL",
                Collections.emptyList());
    }*/

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
    @Getter
    private static class SwaggerPageable {

        @ApiParam(value = "Number of records per page", example = "10")
        @Nullable
        private Integer size;

        @ApiParam(value = "Results page you want to retrieve (0..N)", example = "0")
        @Nullable
        private Integer page;

        @ApiParam(allowMultiple = true, value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.")
        @Nullable
        private String[] sort;

    }


    /*public Docket userApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKeys()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.apu.example.springsecurityjwt"))
//                .paths(regex("/api/user.*"))
                .paths(PathSelectors.any())
                .build();
    }*/
}
