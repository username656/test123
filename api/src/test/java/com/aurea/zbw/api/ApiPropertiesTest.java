package com.aurea.zbw.api;

import com.aurea.zbw.api.ApiProperties;
import com.aurea.zbw.api.test.BasePOJOTest;

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
