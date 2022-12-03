package com.apu.example.springsecurityjwt.config;

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
import java.util.List;


@EnableSwagger2
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
//                .securitySchemes(Arrays.asList(securityScheme()))
                .securitySchemes(Arrays.asList(apiKeys()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.apu.example.springsecurityjwt"))
//                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(Pageable.class, SwaggerPageable.class)
                .directModelSubstitute(Sort.class, String[].class);
    }

    private ApiKey apiKeys(){
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");

    }
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Jwt Spring Security Swagger").description("")
                .termsOfServiceUrl("")
                .contact(new Contact("Apu Kumar Chakroborti", "https://github.com/apuchakroborti", "apuchakroborti50@gmail.com"))
                .license("Open Source").licenseUrl("").version("1.0.0").build();
    }

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

}
