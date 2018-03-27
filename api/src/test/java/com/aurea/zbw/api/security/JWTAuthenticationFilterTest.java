package com.aurea.zbw.api.security;

import com.aurea.zbw.api.security.TokenAuthenticationService;
import com.aurea.zbw.api.security.JWTAuthenticationFilter;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

public class JWTAuthenticationFilterTest {

    private static final String SAMPLE_TOKEN = "";

    @Test
    public void doFilterInternalShouldNotSetAuthenticationWhenNoTokenReceived() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        TokenAuthenticationService tokenAuthenticationService = mock(TokenAuthenticationService.class);
        JWTAuthenticationFilter service = new JWTAuthenticationFilter(tokenAuthenticationService);

        service.doFilterInternal(request, response, filterChain);

        verify(tokenAuthenticationService, never()).getAuthentication(any(String.class));
    }

    @Test
    public void doFilterInternalShouldSetAuthenticationWhenTokenReceived() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(SAMPLE_TOKEN);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        TokenAuthenticationService tokenAuthenticationService = mock(TokenAuthenticationService.class);
        Authentication authentication = mock(Authentication.class);
        when(tokenAuthenticationService.getAuthentication(SAMPLE_TOKEN)).thenReturn(Optional.of(authentication));
        SecurityContext securityContext = mock(SecurityContext.class);
        JWTAuthenticationFilter service = new JWTAuthenticationFilter(tokenAuthenticationService) {
            @Override
            protected SecurityContext getSecurityContext() {
                return securityContext;
            }
        };

        service.doFilterInternal(request, response, filterChain);

        verify(securityContext).setAuthentication(authentication);
    }
    
}
