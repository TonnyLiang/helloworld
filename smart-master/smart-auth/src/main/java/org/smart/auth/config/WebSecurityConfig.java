package org.smart.auth.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smart.auth.security.CustomAuthenticationToken;
import org.smart.auth.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
@Configuration
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	//启动必须资源：继承WebSecurityConfigurerAdapter父类
	@Autowired
	RedisOperationsSessionRepository sessionRepository;
	
	@Autowired
	private AuthenticationProvider CustomAuthenticationProvider;
	
	@Autowired
	private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(CustomAuthenticationProvider);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		sessionRepository.setRedisFlushMode(RedisFlushMode.IMMEDIATE);
		http.csrf().disable().requestMatchers().antMatchers("/**").and().authorizeRequests()
				.antMatchers("/oauth/**","/health", "/filters", "/auditevents", "/env", "/loggers/*", "/heapdump ", "/service-registry/*", "/routes/*", "/dump", "/metrics", "/archaius")
				.permitAll().anyRequest().authenticated().and().formLogin().authenticationDetailsSource(authenticationDetailsSource)
				.loginProcessingUrl("/login").loginPage("/login-page").successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication)
							throws IOException, ServletException {
						httpServletResponse.setContentType("application/json;charset=utf-8");
						PrintWriter out = httpServletResponse.getWriter();
						out.write(resultOk(authentication));
						out.flush();
						out.close();
						// setSessoinData(httpServletRequest);
					}
				}).failureHandler(new AuthenticationFailureHandler() {	
					public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)
							throws IOException, ServletException {
						httpServletResponse.setContentType("application/json;charset=utf-8");
						PrintWriter out = httpServletResponse.getWriter();
						out.write("{\"ec\":\"error\",\"em\":\"登录失败\"}");
						out.flush();
						out.close();
					}
					
				}).and().logout().logoutUrl("/logout").logoutSuccessHandler(new LogoutSuccessHandler() {

					@Override
					public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication)
							throws IOException, ServletException {
						httpServletResponse.setContentType("application/json;charset=utf-8");
						PrintWriter out = httpServletResponse.getWriter();
						out.write("{\"ec\":\"0\",\"em\":\"退出成功\"}");
						out.flush();
						out.close();
					}
				}).invalidateHttpSession(true).and().csrf().disable();
	}
			
	private String resultOk(Authentication authentication) {
		String userJson = "";
		Map m = new HashMap();
		// exclude/include whatever fields you need
		try {
			m.put("ec", "0");
			CustomAuthenticationToken auth = (CustomAuthenticationToken) authentication;
			CustomUserDetails detail = (CustomUserDetails) auth.getPrincipal();
			m.putAll(detail.getData());
			userJson = new ObjectMapper().writeValueAsString(m);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userJson;

	}
}
