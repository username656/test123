package com.aurea.zbw.api.security;

import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This filter does intercept all requests to validate the presence of the JWT through the TokenAuthenticationService.
 */
@RequiredArgsConstructor
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final TokenAuthenticationService tokenAuthenticationService;

    @Override
    public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws IOException, ServletException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null) {
            Optional<Authentication> authentication = tokenAuthenticationService.getAuthentication(token);
            getSecurityContext().setAuthentication(authentication.orElse(null));
        }

        filterChain.doFilter(request, response);
    }

    @VisibleForTesting
    protected SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

}
