package com.aurea.kayako.api.filters;

import static java.util.stream.Collectors.joining;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpMethod.OPTIONS;

import com.aurea.kayako.api.ApiProperties;
import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter extends OncePerRequestFilter {

    @VisibleForTesting
    static final String HEADER_JOINER = ",";

    private final ApiProperties properties;

    @Override
    public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
        throws IOException, ServletException {
        if (properties.getApi().getSecurity().getCors().getAllowedOrigins().contains(request.getHeader(ORIGIN))) {
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(ORIGIN));
        }
        response.setHeader(ACCESS_CONTROL_ALLOW_METHODS,
            properties.getApi().getSecurity().getCors().getAllowedMethods().stream().collect(joining(HEADER_JOINER)));
        response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS,
            properties.getApi().getSecurity().getCors().getAllowedHeaders().stream().collect(joining(HEADER_JOINER)));
        response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS,
            properties.getApi().getSecurity().getCors().getAllowedHeaders().stream().collect(joining(HEADER_JOINER)));

        if (OPTIONS.matches(request.getMethod())) {
            response.setStatus(SC_OK);
            return;
        }

        filterChain.doFilter(request, response);
    }

}
