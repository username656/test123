package com.aurea.zbw.api.security;

import com.aurea.zbw.api.security.TokenAuthenticationService;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.aurea.zbw.api.ApiProperties;
import com.aurea.zbw.api.ApiProperties.Api;
import com.aurea.zbw.api.ApiProperties.Api.Security;
import com.aurea.zbw.api.ApiProperties.Api.Security.Jwt;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class TokenAuthenticationServiceTest {

    private static final String SAMPLE_AUTHORITY = "USER";
    private static final String SAMPLE_USERNAME = "username";
    private static final String SAMPLE_AUTHENTICATION_TYPE = "Bearer";
    private static final ApiProperties SAMPLE_PROPERTIES = new ApiProperties();
    static {
        SAMPLE_PROPERTIES.getApi().setSecurity(new Security());
        SAMPLE_PROPERTIES.getApi().getSecurity().setJwt(new Jwt());
        SAMPLE_PROPERTIES.getApi().getSecurity().getJwt().setExpiration(864000000L);
        SAMPLE_PROPERTIES.getApi().getSecurity().getJwt().setSecret("testSecret");
        SAMPLE_PROPERTIES.getApi().getSecurity().setAuthentication(new com.aurea.zbw.api.ApiProperties.Api.Security.Authentication());
        SAMPLE_PROPERTIES.getApi().getSecurity().getAuthentication().setType(SAMPLE_AUTHENTICATION_TYPE);
    }

    @Test
    public void givenConstructorThenObjectIsCreated() {
        ApiProperties properties = new ApiProperties();
        properties.setApi(EnhancedRandom.random(Api.class));
        assertThat(new TokenAuthenticationService(properties), notNullValue());
    }

    @Test
    public void tokensAreCorrectlyCodedAndDecoded() {
        TokenAuthenticationService object = new TokenAuthenticationService(SAMPLE_PROPERTIES);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(SAMPLE_USERNAME);
        List authority = AuthorityUtils.commaSeparatedStringToAuthorityList(SAMPLE_AUTHORITY);
        when(authentication.getAuthorities()).thenReturn(authority);

        String token = object.getToken(authentication);

        Optional<Authentication> result = object.getAuthentication(token);
        assertThat(result.get().getName(), is(SAMPLE_USERNAME));
        assertThat(result.get().getAuthorities(), contains(new SimpleGrantedAuthority(SAMPLE_AUTHORITY)));
    }

}
