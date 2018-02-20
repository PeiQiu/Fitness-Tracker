package com.example.demo.controller;


import com.example.demo.SecurityConfig;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class MyRememberMeService extends TokenBasedRememberMeServices{

    private static final String CURRENT_NAME = "current_name";

    @Autowired
    private UserService userService;

    public MyRememberMeService(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    public void onLoginSuccess (HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication){
        super.onLoginSuccess(request, response, successfulAuthentication);
        SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
        this.afterOnLoginSuccess(request, response, successfulAuthentication);
    }

    private void afterOnLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication){
        HttpSession session = request.getSession();
//        System.out.println("login success ------------- session id = " + session.getId());
        String userName = successfulAuthentication.getName();
//        System.out.println("login username : " + userName);
        session.setAttribute(CURRENT_NAME, userService.loadUserByUsername(userName));
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response){
        StringBuilder sb = new StringBuilder();
        for(String ct : cookieTokens){
            sb.append(ct).append("  ---  ");
        }
//        System.out.println("cookie token arrays value :: " + sb.toString());
        UserDetails userDetails = super.processAutoLoginCookie(cookieTokens, request, response);
        this.afterProcessAutoLoginCookie(userDetails, request, response);
        return userDetails;
    }

    private void afterProcessAutoLoginCookie(UserDetails userDetails, HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
//        System.out.println(" +++++ auto login success ------------- session id = " + session.getId());
        String username = userDetails.getUsername();
//        System.out.println(" +++++++ auto user name : " + username);
        session.setAttribute(CURRENT_NAME, userService.loadUserByUsername(username));
    }
}
