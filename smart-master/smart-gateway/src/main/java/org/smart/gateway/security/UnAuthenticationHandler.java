package org.smart.gateway.security;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UnAuthenticationHandler implements
		AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = 5932598630249087581L;

	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		try {
			Map m = new HashMap();
			m.put("ec", "401");
			m.put("em", "session not validate!");
			String json = new ObjectMapper().writeValueAsString(m);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
			response.getWriter().write(json);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
