package com.aurea.kayako.api.config;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static springfox.documentation.builders.PathSelectors.regex;

import com.aurea.kayako.api.ApiApplication;
import com.aurea.kayako.api.ApiProperties;
import com.aurea.kayako.api.security.AccountCredentials;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration related to Swagger Documentation endpoint.
 */
@RequiredArgsConstructor
@Import({ SpringDataRestConfiguration.class, BeanValidatorPluginsConfiguration.class })
@EnableSwagger2
@Configuration
public class SwaggerDocumentationConfig {

    private static final String ROOT_PATH = "/";
    @VisibleForTesting
    static final String API_DESCRIPTION_RESOURCE = "api.md";
    private static final String SECURITY_SCHEME_LOCATION = "header";

    public static final String SECURITY_SCHEME_KEY_NAME = "Easier";

    public static final String LOGIN_TAG = "Login";

    @Value("${server.error.path}")
    private String errorPath;

    private final TypeResolver typeResolver;
    private final ApiProperties properties;

    @Bean
    public Docket customImplementation() throws IOException {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            // Excluding the root path from descriptions
            // It is the swagger documentation description
            .paths(and(
                not(regex(ROOT_PATH)),
                not(regex(errorPath))
            ))
            .build()
            .ignoredParameterTypes(AuthenticationPrincipal.class)
            .securitySchemes(securitySchemes())
            .securityContexts(Arrays.asList(securityContext()))
            .apiInfo(apiInfo())
            .additionalModels(typeResolver.resolve(AccountCredentials.class));
    }

    private List<SecurityScheme> securitySchemes() {
        return Arrays.asList(new ApiKey(SECURITY_SCHEME_KEY_NAME, HttpHeaders.AUTHORIZATION, SECURITY_SCHEME_LOCATION));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex("/data.*"))
            .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "To access everything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference(SECURITY_SCHEME_KEY_NAME, authorizationScopes));
    }

    private ApiInfo apiInfo() throws IOException {
        try (InputStream inputStream = ApiApplication.class.getResourceAsStream(API_DESCRIPTION_RESOURCE)) {
            String description = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            return new ApiInfoBuilder()
                .title(properties.getApi().getTitle())
                .description(description)
                .license(properties.getApi().getLicense())
                .version(properties.getApi().getVersion())
                .build();
        }
    }

}
