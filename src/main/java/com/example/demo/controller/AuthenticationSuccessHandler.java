package com.example.demo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.domain.User;
import com.example.demo.services.UserService;
import com.example.demo.tools.CookieTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private UserService userService;
	
	@Override
	public void onAuthenticationSuccess( HttpServletRequest request, HttpServletResponse response, Authentication authentication ) throws JsonProcessingException, IOException {
//		System.out.println(".............enter user research authentication ------ "+ authentication.getName());
		User user = userService.loadUserByUsername(authentication.getName());

//		int loginMaxAge = 30*24*60*60;
//		CookieTool.addCookie(response, "loginUserName", user.getUsername(), loginMaxAge);
//		CookieTool.addCookie(response, "loginPassWord", user.getPassword(), loginMaxAge);

		new Jackson2ObjectMapperBuilder();
		ObjectMapper mapper = Jackson2ObjectMapperBuilder
				.json().build();
		response.setStatus( HttpServletResponse.SC_OK );
		response.getWriter().print( mapper.writeValueAsString( user ) );
		response.getWriter().flush();
	}
}
