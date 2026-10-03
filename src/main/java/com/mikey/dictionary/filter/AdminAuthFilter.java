package com.mikey.dictionary.filter;

import com.mikey.dictionary.exception.BadCredentialsException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Objects;

public class AdminAuthFilter extends OncePerRequestFilter {
    private final String SECRET_KEY;
    private final HandlerExceptionResolver resolver;

    public AdminAuthFilter(String secretKey, HandlerExceptionResolver resolver) {
        SECRET_KEY = secretKey;
        this.resolver = resolver;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String auth = request.getHeader("Authorization");

        if (!Objects.equals(auth, SECRET_KEY)) {
            resolver.resolveException(request,response,null,new BadCredentialsException("You do not have permission to perform this action"));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
