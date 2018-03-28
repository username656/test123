package com.aurea.zbw.api.security;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * This class is used to map request data when a POST call is made to /login path.
 * The request data should contain the username and password as part of the body.
 *
 * @see https://auth0.com/blog/securing-spring-boot-with-jwts/
 */
@ApiModel(description = "This class is used to map request data when a POST call is made to /auth/login path.")
@Data
public final class AccountCredentials {

    private String username;
    private String password;

}
