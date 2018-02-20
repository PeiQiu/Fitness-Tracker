package com.example.demo.filters;

import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CsrfTokenFilter extends OncePerRequestFilter {
		
    protected static final String REQUEST_ATTRIBUTE_NAME = "_csrf";
    protected static final String RESPONSE_HEADER_NAME = "X-CSRF-HEADER";
    protected static final String RESPONSE_PARAM_NAME = "X-CSRF-PARAM";
    protected static final String RESPONSE_TOKEN_NAME = "X-CSRF-TOKEN";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, javax.servlet.FilterChain filterChain) throws ServletException, IOException {
        CsrfToken token = (CsrfToken) request.getAttribute(REQUEST_ATTRIBUTE_NAME);
//        System.out.print("varify token :: " + (token != null) + "token value : ( " + token.getHeaderName() + " , "+ token.getToken() + " )");
        if (token != null) {
//            isAuthenticating(request);
            response.setHeader(RESPONSE_HEADER_NAME, token.getHeaderName());
            response.setHeader(RESPONSE_PARAM_NAME, token.getParameterName());
            response.setHeader(RESPONSE_TOKEN_NAME , token.getToken());
        }
        filterChain.doFilter(request, response);
    }

    private Boolean isAuthenticating(HttpServletRequest request){
        System.out.println(" >>>>>>>>>>>>>" + request.getMethod() + " <> get request url : " + request.getRequestURI());
        return request.getRequestURI().equals("/login");
    }
}