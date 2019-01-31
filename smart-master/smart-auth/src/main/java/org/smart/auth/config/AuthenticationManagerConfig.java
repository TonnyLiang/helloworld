package org.smart.auth.config;

import org.smart.auth.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
@Configuration
public class AuthenticationManagerConfig extends
		GlobalAuthenticationConfigurerAdapter {
	@Autowired
	CustomAuthenticationProvider customAuthenticationProvider;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customAuthenticationProvider);
	}
}
