package com.example.demo.controller;


import com.example.demo.domain.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class LoginInterceptor implements HandlerInterceptor{


    @Autowired
    private UserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        User loginUser = (User) request.getSession().getAttribute("user");
//        System.out.println("process the request -------------- ////// ");
        if (loginUser == null) {
            String loginCookieUserName = "";
            String loginCookiePassword = "";

            Cookie[] cookies = request.getCookies();
            if (null != cookies) {
                for (Cookie cookie : cookies) {
                    if ("loginUserName".equals(cookie.getName())) {
                        loginCookieUserName = cookie.getValue();
                    } else if ("loginPassWord".equals(cookie.getName())) {
                        loginCookiePassword = cookie.getValue();
                    }
                }
                if (!"".equals(loginCookieUserName) && !"".equals(loginCookiePassword)) {
                    User user = userService.loadUserByUsername(loginCookieUserName);
                    if (loginCookiePassword.equals(user.getPassword())) {
                        request.getSession().setAttribute("user", user);
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
