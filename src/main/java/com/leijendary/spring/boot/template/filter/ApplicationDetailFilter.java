package com.leijendary.spring.boot.template.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.leijendary.spring.boot.template.config.properties.InfoProperties;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationDetailFilter extends OncePerRequestFilter {

    public static final String HEADER_APP_NAME = "X-App-Name";
    public static final String HEADER_APP_DESCRIPTION = "X-App-Description";
    public static final String HEADER_APP_VERSION = "X-App-Version";

    private final InfoProperties infoProperties;

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain chain) throws ServletException, IOException {
        final var app = infoProperties.getApp();

        response.addHeader(HEADER_APP_NAME, app.getName());
        response.addHeader(HEADER_APP_DESCRIPTION, app.getDescription());
        response.addHeader(HEADER_APP_VERSION, app.getVersion());

        chain.doFilter(request, response);
    }
}
