package com.aurea.zbw.api.security;

import com.aurea.zbw.api.security.AccountCredentials;
import com.aurea.zbw.api.security.JWTLoginFilter;
import com.aurea.zbw.api.security.TokenAuthenticationService;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aurea.zbw.api.ApiProperties;
import com.aurea.zbw.api.ApiProperties.Api;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class JWTLoginFilterTest {

    private static final String SAMPLE_USERNAME = "sample";
    private static final String SAMPLE_PASSWORD = "secret";
    private static final String SAMPLE_TOKEN = "token";
    private static final String SAMPLE_MESSAGE = "message";

    @Test
    public void attemptAuthenticationCorrectlyDelegatesToAuthenticationManager() throws Exception {
        ApiProperties properties = new ApiProperties();
        properties.setApi(EnhancedRandom.random(Api.class));
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        TokenAuthenticationService tokenAuthenticationService = mock(TokenAuthenticationService.class);
        JWTLoginFilter object = new JWTLoginFilter(properties, authenticationManager, tokenAuthenticationService) {
            @Override
            AccountCredentials getAccountCredentialsFromRequest(final HttpServletRequest request) throws IOException {
                AccountCredentials credentials = new AccountCredentials();
                credentials.setUsername(SAMPLE_USERNAME);
                credentials.setPassword(SAMPLE_PASSWORD);
                return credentials;
            }
        };
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        object.attemptAuthentication(request, response);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> argument = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(argument.capture());
        assertThat(argument.getValue().getPrincipal(), is(SAMPLE_USERNAME));
        assertThat(argument.getValue().getCredentials(), is(SAMPLE_PASSWORD));
        assertThat(argument.getValue().getAuthorities(), is(Collections.emptyList()));
    }

    @Test
    public void attemptAuthenticationShouldThrowInternalAuthenticationServiceExceptionWhenIOException() throws Exception {
        IOException sampleException = new IOException(SAMPLE_MESSAGE);
        try {
            ApiProperties properties = new ApiProperties();
            properties.setApi(EnhancedRandom.random(Api.class));
            AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
            TokenAuthenticationService tokenAuthenticationService = mock(TokenAuthenticationService.class);
            JWTLoginFilter object = new JWTLoginFilter(properties, authenticationManager, tokenAuthenticationService) {
                @Override
                AccountCredentials getAccountCredentialsFromRequest(final HttpServletRequest request) throws IOException {
                    throw sampleException;
                }
            };
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);

            object.attemptAuthentication(request, response);

            fail("It should throw exception as per the scenario design.");
        } catch (InternalAuthenticationServiceException ex) {
            assertThat(ex.getMessage(), is(SAMPLE_MESSAGE));
            assertThat(ex.getCause(), is(sampleException));
        }
    }

    @Test
    public void successfulAuthenticationAddsTheTokenReturnedByTheAuthenticationService() throws Exception {
        ApiProperties properties = new ApiProperties();
        properties.setApi(EnhancedRandom.random(Api.class));
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        TokenAuthenticationService tokenAuthenticationService = mock(TokenAuthenticationService.class);
        JWTLoginFilter object = new JWTLoginFilter(properties, authenticationManager, tokenAuthenticationService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        Authentication authentication = mock(Authentication.class);
        when(tokenAuthenticationService.getToken(authentication)).thenReturn(SAMPLE_TOKEN);

        object.successfulAuthentication(request, response, chain, authentication);

        ArgumentCaptor<String> headerNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> headerValueCaptor = ArgumentCaptor.forClass(String.class);
        verify(response, times(2)).addHeader(headerNameCaptor.capture(), headerValueCaptor.capture());
        assertThat(headerNameCaptor.getAllValues().get(0),
            is(properties.getApi().getSecurity().getAuthentication().getResponseHeader()));
        assertThat(headerValueCaptor.getAllValues().get(0),
            is(properties.getApi().getSecurity().getAuthentication().getType() + " " + SAMPLE_TOKEN));
        assertThat(headerNameCaptor.getAllValues().get(1), is(HttpHeaders.CONTENT_TYPE));
        assertThat(headerValueCaptor.getAllValues().get(1), is(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

}
