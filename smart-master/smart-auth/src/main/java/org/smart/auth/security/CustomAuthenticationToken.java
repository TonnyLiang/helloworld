package org.smart.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

	@Autowired
	private CustomUserDetails userDetails;

	public CustomAuthenticationToken(CustomUserDetails userDetails) {
		super(null);
		this.userDetails = userDetails;
		super.setAuthenticated(true);
	}

	public Object getPrincipal() {
		return this.userDetails;
	}

	public Object getCredentials() {
		return this.userDetails.getPassword();
	}

}
