package com.mikey.dictionary.filter;

import com.mikey.dictionary.exception.BadCredentialsException;
import com.mikey.dictionary.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver resolver;
    private final ApiKeyRepository repository;

    public ApiKeyAuthFilter(HandlerExceptionResolver resolver, ApiKeyRepository repository) {
        this.resolver = resolver;
        this.repository = repository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader("api-key");
        if(apiKey == null || apiKey.isBlank()){
            resolver.resolveException(request,response,null,new BadCredentialsException("You do not have permission to perform this action"));
            return;
        }
        if (!repository.existsByApiKeyAndIsActiveTrue(apiKey)){
            resolver.resolveException(request,response,null,new BadCredentialsException("Invalid or inactive api-key"));
            return;
        }
        filterChain.doFilter(request,response);
    }
}
