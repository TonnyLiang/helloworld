package org.smart.auth.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@ConfigurationProperties(prefix = "auth")
public class CustomAuthenticationProvider implements AuthenticationProvider {
	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
	
	List<LoginService> loginService;

	public List<LoginService> getLoginService() {
		return loginService;
	}


	public void setLoginService(List<LoginService> loginService) {
		this.loginService = loginService;
	}


	private LoginService getAuthService(String authType) {
		for (LoginService type : loginService) {
			if (type.getType().equals(authType)) {
				return type;
			}
		}
		return null;
	}
	
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		String username = authentication.getName();
		Map data = null;
		if (authentication.getDetails() instanceof CustomWebAuthenticationDetails) {
			CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
			data = details.getData();
		} else {
			data = (Map) authentication.getDetails();

		}
		String password = (String) authentication.getCredentials();
		String authtype = (String) data.get("authtype");
		LoginService service = getAuthService(authtype);
		Map sendData = new HashMap();
		sendData.putAll(data);
		sendData.put("password", password);

		if (service == null) {
			throw new BadCredentialsException("-1");
		}
		String serviceName = service.getService();
		String Uri = service.getURI();

//		ServiceInstance serviceInstance = loadBalancerClient.choose(serviceName);
//		if (serviceInstance == null) {
//			throw new RuntimeException("Failed to choose an auth instance.");
//		}
		HttpHeaders headers = new HttpHeaders();
		Map<String, Object> map=null;
		try {
			map = postForMap( "http://" + serviceName + "/" + Uri, sendData, headers);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		String code = (String) map.get("ec");
		if (!code.equals("0")) {
			throw new BadCredentialsException(code);
		}
		String clientId = (String) data.get("client");
		String userId = (String) ((Map) map.get("data")).get("userId");		
		if (userId==null||username==null) {
			throw new BadCredentialsException("401");
		}
//		authentication.detail.cleanData
		CustomUserDetails customUserDetails = buildCustomUserDetails(username, password, userId, clientId, (Map) map.get("data"));
		return new CustomAuthenticationToken(customUserDetails);
	}
	private CustomUserDetails buildCustomUserDetails(String username, String password, String userId, String clientId, Map data) {
		CustomUserDetails customUserDetails = new CustomUserDetails.CustomUserDetailsBuilder().withUserId(userId).withPassword(password).withUsername(username).withData(data)
				.withClientId(clientId).build();
		return customUserDetails;
	}
	private Map<String, Object> postForMap(String path, Map<String, String> formData, HttpHeaders headers) {
		if (restTemplate == null)
			restTemplate = restTemplate();
		if (headers.getContentType() == null) {
			headers.setContentType(MediaType.APPLICATION_JSON);
		}
		logger.info("path="+path);
		@SuppressWarnings("rawtypes")
		Map map = restTemplate.exchange(path, HttpMethod.POST, new HttpEntity<Map<String, String>>(formData, headers), Map.class).getBody();
		@SuppressWarnings("unchecked")
		Map<String, Object> result = map;
		return result;
	}
	@Autowired
	private RestTemplate restTemplate;

	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		//return true;
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
	public static class LoginService{
		private String type;
		private String URI;
		private String service;
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getURI() {
			return URI;
		}
		public void setURI(String uRI) {
			URI = uRI;
		}
		public String getService() {
			return service;
		}
		public void setService(String service) {
			this.service = service;
		}
		
	}
}
