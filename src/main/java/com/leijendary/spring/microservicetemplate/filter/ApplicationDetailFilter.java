package com.leijendary.spring.microservicetemplate.filter;

import com.leijendary.spring.microservicetemplate.config.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApplicationDetailFilter extends OncePerRequestFilter {

    public static final String HEADER_APP_NAME = "X-App-Name";
    public static final String HEADER_APP_VERSION = "X-App-Version";

    private final ApplicationProperties applicationProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        response.addHeader(HEADER_APP_NAME, applicationProperties.getName());
        response.addHeader(HEADER_APP_VERSION, applicationProperties.getVersion());

        chain.doFilter(request, response);
    }
}
