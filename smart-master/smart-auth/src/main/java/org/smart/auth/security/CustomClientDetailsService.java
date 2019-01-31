package org.smart.auth.security;

import javax.annotation.PostConstruct;

import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Component;

@Component
public class CustomClientDetailsService implements ClientDetailsService {
	//启动必须资源：实现 ClientDetailsService接口(客户端)
	private ClientDetailsService clientDetailsService;  
	
    @PostConstruct  
    public void init() {  
        InMemoryClientDetailsServiceBuilder inMemoryClientDetailsServiceBuilder = new InMemoryClientDetailsServiceBuilder();  
        // @formatter:off  
        inMemoryClientDetailsServiceBuilder.  
            withClient("tonr")  
                .resourceIds("user")  
                .authorizedGrantTypes("authorization_code", "implicit")  
                .authorities("ROLE_CLIENT")  
                .scopes("read", "write")  
                .secret("secret")  
                .and()  
            .withClient("tonr-with-redirect")  
                .resourceIds("user")  
                .authorizedGrantTypes("authorization_code", "implicit")  
                .authorities("ROLE_CLIENT")  
                .scopes("read", "write")  
                .secret("secret")  
                // .redirectUris(tonrRedirectUri)  
                .and()  
            .withClient("my-client-with-registered-redirect")  
                .resourceIds("user")  
                .authorizedGrantTypes("authorization_code", "client_credentials")  
                .authorities("ROLE_CLIENT")  
                .scopes("read", "trust")  
                .redirectUris("http://anywhere?key=value")  
                .and()  
            .withClient("my-trusted-client")  
                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")  
                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")  
                .scopes("read", "write", "trust")  
                .accessTokenValiditySeconds(60)  
                .and()  
            .withClient("my-trusted-client-with-secret")  
                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")  
                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")  
                .scopes("read", "write", "trust")  
                .secret("somesecret")  
                .and()  
            .withClient("frontend0")  
                .secret("frontend0")
                .authorizedGrantTypes("password", "refresh_token")  
                .scopes("all")  
                .and()  
            .withClient("my-less-trusted-autoapprove-client")  
                .authorizedGrantTypes("implicit")  
                .authorities("ROLE_CLIENT")  
                .scopes("read", "write", "trust")  
                .autoApprove(true);  
        // @formatter:on  
        try {  
            clientDetailsService = inMemoryClientDetailsServiceBuilder.build();  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
  
    @Override  
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {  
  //      System.out.println("loadClientByClientId:" + clientId + "  ----------------------");  
        return clientDetailsService.loadClientByClientId(clientId);  
    } 

}
