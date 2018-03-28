package com.aurea.zbw.api.filters;

import com.aurea.zbw.api.filters.SimpleCORSFilter;

import static com.aurea.zbw.api.filters.SimpleCORSFilter.HEADER_JOINER;
import static java.util.stream.Collectors.joining;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.aurea.zbw.api.ApiProperties;
import com.aurea.zbw.api.ApiProperties.Api.Security;
import com.aurea.zbw.api.ApiProperties.Api.Security.Cors;
import com.google.common.collect.ImmutableList;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class SimpleCORSFilterTest {

    private static final ApiProperties SAMPLE_PROPERTIES = new ApiProperties();
    static {
        SAMPLE_PROPERTIES.getApi().setSecurity(new Security());
        SAMPLE_PROPERTIES.getApi().getSecurity().setCors(new Cors());
        SAMPLE_PROPERTIES.getApi().getSecurity().getCors().setAllowedOrigins(ImmutableList.of("http://localhost:4200"));
        SAMPLE_PROPERTIES.getApi().getSecurity().getCors().setAllowedMethods(ImmutableList.of("Content-Type"));
        SAMPLE_PROPERTIES.getApi().getSecurity().getCors().setAllowedHeaders(ImmutableList.of("POST", "PUT", "GET", "OPTIONS", "DELETE"));
    }

    private SimpleCORSFilter object;
    private HttpServletRequest req;
    private HttpServletResponse res;
    private FilterChain chain;

    @Before
    public void executeBeforEach() {
        object = new SimpleCORSFilter(SAMPLE_PROPERTIES);

        req = mock(HttpServletRequest.class);
        res = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
    }

    @Test
    public void test_doFilterInternal_AddsTheExpectedHeaders_HeadersAdded() throws Exception {
        when(req.getMethod()).thenReturn(HttpMethod.GET.name());
        when(req.getHeader(HttpHeaders.ORIGIN)).thenReturn(
            SAMPLE_PROPERTIES.getApi().getSecurity().getCors().getAllowedOrigins().stream().collect(joining(HEADER_JOINER))
        );

        object.doFilterInternal(req, res, chain);

        verify(res, times(1)).setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
            SAMPLE_PROPERTIES.getApi().getSecurity().getCors().getAllowedOrigins().stream().collect(joining(HEADER_JOINER)));
        verify(res, times(1)).setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
            SAMPLE_PROPERTIES.getApi().getSecurity().getCors().getAllowedMethods().stream().collect(joining(HEADER_JOINER)));
        verify(res, times(1)).setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
            SAMPLE_PROPERTIES.getApi().getSecurity().getCors().getAllowedHeaders().stream().collect(joining(HEADER_JOINER)));
        verify(res, times(1)).setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
            SAMPLE_PROPERTIES.getApi().getSecurity().getCors().getAllowedHeaders().stream().collect(joining(HEADER_JOINER)));
        verifyNoMoreInteractions(res);

        verify(chain, times(1)).doFilter(req, res);
        verifyNoMoreInteractions(chain);
    }

    @Test
    public void test_doFilterInternal_NoAllowedOriginIsAddedIfNoOriginHeaderPresent_HeaderNotPresent()
        throws Exception {
        when(req.getMethod()).thenReturn(HttpMethod.GET.name());

        object.doFilterInternal(req, res, chain);

        verify(res, never()).setHeader(eq(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN), any(String.class));
    }

    @Test
    public void test_doFilterInternal_OptionsDoNotChaing_ReturnWithoutCallChainDoFilter() throws Exception {
        when(req.getMethod()).thenReturn(HttpMethod.OPTIONS.name());

        object.doFilterInternal(req, res, chain);

        verify(chain, never()).doFilter(req, res);
        verifyNoMoreInteractions(chain);
    }

}
