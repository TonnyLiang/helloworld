package org.smart.auth.config;


import org.smart.auth.exception.CustomWebResponseExceptionTranslator;
import org.smart.auth.security.CustomAuthorizationTokenServices;
import org.smart.auth.security.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {
	//启动必须资源：继承AuthorizationServerConfigurerAdapter父类(授权服务器)
	@Value("${auth.accessTokenValiditySeconds}")
	int accessTokenValidity;

	@Value("${auth.refreshTokenValiditySeconds}")
	int refreshTokenValidity;

	 
	@Autowired
	@Qualifier("customClientDetailsService")
	ClientDetailsService customClientDetailsService;

	@Autowired
	private RedisConnectionFactory connectionFactory;

	@Autowired
	private WebResponseExceptionTranslator webResponseExceptionTranslator;

	@Bean
	public WebResponseExceptionTranslator webResponseExceptionTranslator() {
		return new CustomWebResponseExceptionTranslator();
	}

	@Bean
	public TokenStore tokenStore() {
		RedisTokenStore redis = new RedisTokenStore(connectionFactory);
		return redis;
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(customClientDetailsService);
	}

	//// 配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactory
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore()).tokenServices(authorizationServerTokenServices())
				.accessTokenConverter(accessTokenConverter()).exceptionTranslator(webResponseExceptionTranslator);
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new CustomTokenEnhancer();
		converter.setSigningKey("secret");
		return converter;
	}

	@Bean
	public AuthorizationServerTokenServices authorizationServerTokenServices() {
		CustomAuthorizationTokenServices customTokenServices = new CustomAuthorizationTokenServices();
		customTokenServices.setTokenStore(tokenStore());
		customTokenServices.setSupportRefreshToken(true);
		customTokenServices.setReuseRefreshToken(true);
		customTokenServices.setClientDetailsService(customClientDetailsService);
		if(accessTokenValidity!=0)
		customTokenServices.setAccessTokenValiditySeconds(accessTokenValidity);
		if(refreshTokenValidity!=0)
		customTokenServices.setRefreshTokenValiditySeconds(refreshTokenValidity);
		customTokenServices.setTokenEnhancer(accessTokenConverter());
		return customTokenServices;
	}
}
