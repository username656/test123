package com.aurea.boot.autoconfigure.api.filter;

import static org.apache.commons.lang3.CharEncoding.UTF_8;

import com.aurea.boot.autoconfigure.api.config.props.ApiProps;
import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class CorsFilter implements Filter {

    @NonNull
    private final ApiProps apiProps;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        if (apiProps.getCors().getAllowedOrigins()
                .contains(request.getHeader(HttpHeaders.ORIGIN))) {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                    URLEncoder.encode(request.getHeader(HttpHeaders.ORIGIN), UTF_8));
        }
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                URLEncoder.encode(apiProps.getCors().getAllowedMethods(), UTF_8));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                URLEncoder.encode(apiProps.getCors().getAllowedHeaders(), UTF_8));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE,
                URLEncoder.encode(apiProps.getCors().getMaxAge(), UTF_8));
        if (HttpMethod.OPTIONS.matches(((HttpServletRequest) req).getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
        // emtpy destroy
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        // empty init
    }
}
