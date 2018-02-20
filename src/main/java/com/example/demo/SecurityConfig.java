package com.example.demo;



import com.example.demo.controller.AuthenticationSuccessHandler;
import com.example.demo.controller.FitnessAuthenticationFailureHandler;
import com.example.demo.controller.HttpAuthenticationEntryPoint;
import com.example.demo.controller.MyRememberMeService;
import com.example.demo.filters.CsrfTokenFilter;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String CURRENT_USER = "current_name";

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationSuccessHandler successHandler;
	
	@Autowired
	private HttpAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private FitnessAuthenticationFailureHandler failureHandler;

	@Autowired
	private MyRememberMeService rememberMeService;

	@Autowired
	private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;
	
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf();
		http
			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint)
		.and()
			.addFilterAfter(new CsrfTokenFilter(), CsrfFilter.class)
			.authorizeRequests()
//			.antMatchers("/resources/**", "/login", "/").permitAll()
			.antMatchers("/resources/**", "/**").permitAll()
			.anyRequest().authenticated()
		.and()

		.formLogin()
			.loginPage("/")
			.loginProcessingUrl("/login")
			.usernameParameter("username")
			.passwordParameter("password")
			.successHandler( successHandler )
			.failureHandler( failureHandler )
			.permitAll()
		.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//			.invalidSessionUrl("/invalidSession.html")
//			.maximumSessions(1).sessionRegistry(sessionRegistry()).and()
//			.sessionFixation().none()

		.and()
		.logout()
//			.logoutUrl("/logout")
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.invalidateHttpSession(true)
			.clearAuthentication(true)
				.deleteCookies("JSESSIONID")
//				.deleteCookies("loginUserName").deleteCookies("loginPassWord")
			.permitAll()
		.and()
			.rememberMe()
				.rememberMeServices(rememberMeService);
	}
	
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	    auth.userDetailsService(userService); //.passwordEncoder(encoder);
		auth.authenticationProvider(rememberMeAuthenticationProvider);
	}


	@Bean
	public SessionRegistry sessionRegistry(){
		return new SessionRegistryImpl();
	}

	@Bean
	public RememberMeAuthenticationProvider rememberMeAuthenticationProvider(){
		return new RememberMeAuthenticationProvider(CURRENT_USER);
	}

	@Bean
	public MyRememberMeService myRememberMeService(){
		MyRememberMeService service = new MyRememberMeService(CURRENT_USER, userService);
		service.setAlwaysRemember(true);
		return service;
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

}