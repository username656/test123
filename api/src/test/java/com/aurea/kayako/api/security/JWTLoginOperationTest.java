package com.aurea.kayako.api.security;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.aurea.kayako.api.ApiProperties;
import org.junit.Test;
import springfox.documentation.spi.DocumentationType;

public class JWTLoginOperationTest {

    @Test
    public void supportsShouldAllowOnlySwagger2() {
        JWTLoginOperation object = new JWTLoginOperation(new ApiProperties());

        assertThat(object.supports(DocumentationType.SWAGGER_12), is(false));
        assertThat(object.supports(DocumentationType.SWAGGER_2), is(true));
        assertThat(object.supports(DocumentationType.SPRING_WEB), is(false));
    }

}
