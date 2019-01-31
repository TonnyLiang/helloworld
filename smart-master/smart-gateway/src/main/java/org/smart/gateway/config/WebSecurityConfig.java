package org.smart.gateway.config;

import org.smart.gateway.filter.LoginCheckFilter;
import org.smart.gateway.security.PermitAllUrlProperties;
import org.smart.gateway.security.UnAuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Component;

@Component
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	RedisOperationsSessionRepository sessionRepository;
	
	@Autowired
	private PermitAllUrlProperties permitAllUrlProperties; 
	
	@Autowired
	private UnAuthenticationHandler unAuthenrizeHandler;
	
	@Override
	public void configure(HttpSecurity http) throws Exception{
		sessionRepository.setRedisFlushMode(RedisFlushMode.IMMEDIATE);
		//处理非法请求
		http.csrf().disable().exceptionHandling().authenticationEntryPoint(unAuthenrizeHandler);
		// 处理特定请求 permitall / authenticated
		http.csrf().disable().requestMatchers().antMatchers("/**").and().authorizeRequests()
		.antMatchers(permitAllUrlProperties.getPermitallPatterns())
		.permitAll()   /// 自定义的permitall  auth/**,/css/**,/html/**,/xml/**,/inner/**,/img/**,/js/**,/*.js,/mvc/**,/*.html
		.antMatchers("/health","/filters","/auditevents","/env","/loggers/*","/heapdump ","/service-registry/*","/routes/*","/dump","/metrics","/archaius")		      
		.permitAll()
		.anyRequest().authenticated();
		
		http.addFilterAfter(new LoginCheckFilter(), SecurityContextPersistenceFilter.class);
	}
}
