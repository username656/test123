package com.aurea.kayako.api.security;

import static com.aurea.kayako.api.config.SwaggerDocumentationConfig.LOGIN_TAG;
import static org.springframework.http.HttpMethod.POST;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.aurea.kayako.api.ApiProperties;
import com.aurea.kayako.api.model.User;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;

import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Header;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@RequiredArgsConstructor
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class JWTLoginOperation implements ApiListingScannerPlugin {

    private static final String AUTHORIZATION_HEADER_DESCRIPTION = "JWT Autorization Token";
    private static final String LOGIN_OPERATION_SUMMARY = "login";
    private static final String LOGIN_OPERATION_DESCRIPTION = "Login Endpoint";
    private static final Parameter BODY_PARAMETER = new ParameterBuilder()
        .name("body")
        .parameterType("body")
        .required(true)
        .modelRef(new ModelRef(AccountCredentials.class.getSimpleName()))
        .type(new TypeResolver().resolve(AccountCredentials.class))
        .build();

    private final ApiProperties properties;

    @Override
    public List<ApiDescription> apply(final DocumentationContext context) {
        return ImmutableList.of(new ApiDescription(
            properties.getApi().getSecurity().getAuthentication().getLoginPath(),
            LOGIN_OPERATION_DESCRIPTION,
            ImmutableList.of(new OperationBuilder(new CachingOperationNameGenerator())
                .summary(LOGIN_OPERATION_SUMMARY)
                .method(POST)
                .parameters(ImmutableList.of(BODY_PARAMETER))
                .responseMessages(ImmutableSet.of(
                    new ResponseMessage(HTTP_OK, OK.getReasonPhrase(),
                        new ModelRef(User.class.getSimpleName()),
                        ImmutableMap.of(properties.getApi().getSecurity().getAuthentication().getResponseHeader(),
                            new Header(properties.getApi().getSecurity().getAuthentication().getResponseHeader(),
                                AUTHORIZATION_HEADER_DESCRIPTION,
                                new ModelRef("string"))),
                        Collections.emptyList()
                    ),
                    new ResponseMessage(HTTP_UNAUTHORIZED, UNAUTHORIZED.getReasonPhrase(),
                        null,
                        Collections.emptyMap(),
                        Collections.emptyList()
                    )
                ))
                .tags(ImmutableSet.of(LOGIN_TAG))
                .build()),
            false));
    }

    @Override
    public boolean supports(final DocumentationType delimiter) {
        return DocumentationType.SWAGGER_2.equals(delimiter);
    }

}
