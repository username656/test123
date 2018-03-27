package com.aurea.kayako.api;

import com.aurea.kayako.api.test.BasePOJOTest;

public class ApiPropertiesTest extends BasePOJOTest {

    public ApiPropertiesTest() {
        super(ApiProperties.class,
            ApiProperties.Api.class,
            ApiProperties.Api.Swagger.class,
            ApiProperties.Api.Security.class,
            ApiProperties.Api.Security.Authentication.class,
            ApiProperties.Api.Security.Jwt.class);
    }

}
