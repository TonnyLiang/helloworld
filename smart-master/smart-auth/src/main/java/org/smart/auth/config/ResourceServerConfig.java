package org.smart.auth.config;


import org.smart.auth.security.CustomLogoutHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.client.RestTemplate;
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	//启动必须资源：继承ResourceServerConfigurerAdapter父类(资源服务器)，装配RestTemplate
	@Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .and()
                .requestMatchers().antMatchers("/**")
                .and().authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .addLogoutHandler(customLogoutHandler());
        
        //http.antMatcher("/api/**").addFilterAt(customSecurityFilter(), FilterSecurityInterceptor.class);

    } 
	@Bean
    public CustomLogoutHandler customLogoutHandler() {
        return new CustomLogoutHandler();
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
    }
    
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
    	return new RestTemplate();
    }
}
