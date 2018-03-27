package com.aurea.kayako.api.security;

import com.aurea.kayako.api.ApiProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * This class will intercept POST requests on the /auth/login path and attempt to authenticate the user.
 * When the user is successfully authenticated, it will return a JWT in the Authorization header of the response.
 * @see TokenAuthenticationService
 */
@Slf4j
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ApiProperties properties;
    private final TokenAuthenticationService tokenAuthenticationService;

    public JWTLoginFilter(final ApiProperties properties, final AuthenticationManager authManager,
        final TokenAuthenticationService tokenAuthenticationService) {
        super(new AntPathRequestMatcher(properties.getApi().getSecurity().getAuthentication().getLoginPath()));

        this.properties = properties;
        this.tokenAuthenticationService = tokenAuthenticationService;

        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            AccountCredentials creds = getAccountCredentialsFromRequest(request);

            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                    creds.getUsername(),
                    creds.getPassword(),
                    Collections.emptyList()
                )
            );
        } catch (IOException ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain,
        final Authentication authentication) throws IOException, ServletException {
        addAuthentication(response, authentication);
        addUserData(response, authentication);

        log.info("User {} was successfully authenticated, roles: {}.", authentication.getName(), authentication.getAuthorities());
    }

    @Override
    protected void unsuccessfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
        final AuthenticationException failed) throws IOException, ServletException {

        super.unsuccessfulAuthentication(request, response, failed);

        log.warn("Authentication request failed: {}", failed.toString());
    }

    @VisibleForTesting
    AccountCredentials getAccountCredentialsFromRequest(final HttpServletRequest request) throws IOException {
        return OBJECT_MAPPER.readValue(request.getInputStream(), AccountCredentials.class);
    }

    private void addAuthentication(final HttpServletResponse response, final Authentication authentication) {
        String token = tokenAuthenticationService.getToken(authentication);
        response.addHeader(properties.getApi().getSecurity().getAuthentication().getResponseHeader(),
            properties.getApi().getSecurity().getAuthentication().getType() + " " + token);
    }

    private void addUserData(final HttpServletResponse response, final Authentication authentication) throws IOException {
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);

        if (authentication.getPrincipal() != null) {
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            response.getWriter().print(OBJECT_MAPPER.writeValueAsString(myUserDetails.getUser()));
        }
    }

}
