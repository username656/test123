package com.aurea.kayako.api.config;

import static com.aurea.kayako.api.config.SwaggerDocumentationConfig.API_DESCRIPTION_RESOURCE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import com.aurea.kayako.api.ApiApplication;
import com.aurea.kayako.api.ApiProperties;
import com.fasterxml.classmate.TypeResolver;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;

public class SwaggerDocumentationConfigTest {

    private static final String SAMPLE_TITLE = "sample-title";
    private static final String SAMPLE_LICENSE = "sample-license";
    private static final String SAMPLE_VERSION = "sample-version";

    @Test
    public void customImplementationShouldProduceTheExpectedDoklet() throws Exception {
        TypeResolver typeResolver = mock(TypeResolver.class);
        ApiProperties properties = new ApiProperties();
        properties.getApi().setTitle(SAMPLE_TITLE);
        properties.getApi().setLicense(SAMPLE_LICENSE);
        properties.getApi().setVersion(SAMPLE_VERSION);

        SwaggerDocumentationConfig instance = new SwaggerDocumentationConfig(typeResolver, properties);

        Docket result = instance.customImplementation();

        ApiInfo apiInfo = (ApiInfo) ReflectionTestUtils.getField(result, "apiInfo");
        assertThat(apiInfo.getTitle(), is(SAMPLE_TITLE));
        try (InputStream inputStream = ApiApplication.class.getResourceAsStream(API_DESCRIPTION_RESOURCE)) {
            assertThat(apiInfo.getDescription(), is(IOUtils.toString(inputStream, StandardCharsets.UTF_8)));
        }
        assertThat(apiInfo.getLicense(), is(SAMPLE_LICENSE));
        assertThat(apiInfo.getVersion(), is(SAMPLE_VERSION));
    }

}
