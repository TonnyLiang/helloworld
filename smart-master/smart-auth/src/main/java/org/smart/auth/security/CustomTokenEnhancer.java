package org.smart.auth.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

public class CustomTokenEnhancer extends JwtAccessTokenConverter implements Serializable{
	private static int authenticateCodeExpiresTime = 60 * 60 * 60;

    private static final String TOKEN_SEG_USER_ID = "EMP-ID";
    private static final String TOKEN_SEG_CLIENT = "EMP-ClientId";

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
                                     OAuth2Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        authentication.getUserAuthentication().getPrincipal();
        Map<String, Object> info = new HashMap<>();
        info.put(TOKEN_SEG_USER_ID, userDetails.getUserId());
        info.put("data", userDetails.getData());

        DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
        customAccessToken.setAdditionalInformation(info);

        OAuth2AccessToken enhancedToken = super.enhance(customAccessToken, authentication);
        //enhancedToken.getAdditionalInformation().put(TOKEN_SEG_CLIENT, userDetails.getClientId());
        return enhancedToken;
    }
}
