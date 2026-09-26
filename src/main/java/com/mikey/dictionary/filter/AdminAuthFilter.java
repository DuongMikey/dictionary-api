package com.mikey.dictionary.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

public class AdminAuthFilter extends OncePerRequestFilter {
    private final String SECRETE_KEY;

    public AdminAuthFilter(String secreteKey) {
        SECRETE_KEY = secreteKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String auth = request.getHeader("Auth");

        if (!Objects.equals(auth, SECRETE_KEY)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("error: Unauthorized: you dont have permission to do this");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
