package org.smart.gateway.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.gateway.security.PermitAllUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;

public class HeaderEnhanceFilter implements Filter{

	public static final String USER_ID_IN_HEADER = "EMP-ID";
	public static final String USER_clientIP = "clientIP";

	private static final Logger LOGGER = LoggerFactory.getLogger(HeaderEnhanceFilter.class);

	private static final String ANONYMOUS_USER_ID = "anonymous";

	@Autowired
	private PermitAllUrlProperties permitAllUrlProperties;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		
		String authorization = ((HttpServletRequest) servletRequest).getHeader("Authorization");
		String requestURI = ((HttpServletRequest) servletRequest).getRequestURI();
		
		// 如果是permitall准入url，并且不是认证url，去除请求头的authorization
		if (isPermitAllUrl(requestURI) && isNotOAuthEndpoint(requestURI)) {
			HttpServletRequest resetRequest = removeValueFromRequestHeader((HttpServletRequest) servletRequest);
			filterChain.doFilter(resetRequest, servletResponse);
			return;
		}
	}

	private boolean isPermitAllUrl(String url) {
		return permitAllUrlProperties.isPermitAllUrl(url);
	}
	private boolean isNotOAuthEndpoint(String requestURI) {
		return !requestURI.contains("/auth");
	}
	
	private HttpServletRequestWrapper removeValueFromRequestHeader(HttpServletRequest request) {
		HttpServletRequestWrapper httpServletRequestWrapper = new HttpServletRequestWrapper(request) {
			private Set<String> headerNameSet;
			private Set<String> headerSet;

			@Override
			public Enumeration<String> getHeaderNames() {
				if (headerNameSet == null) {
					// first time this method is called, cache the wrapped
					// request's header names:
					headerNameSet = new HashSet();
					Enumeration<String> wrappedHeaderNames = super.getHeaderNames();
					while (wrappedHeaderNames.hasMoreElements()) {
						String headerName = wrappedHeaderNames.nextElement();
						//不包括 Authorization 的请求头
						if (!"Authorization".equalsIgnoreCase(headerName)) {
							headerNameSet.add(headerName);
						}
					}
					// set default header name value of tenant id and user id
					headerNameSet.add(USER_ID_IN_HEADER);
				}

				return Collections.enumeration(headerNameSet);
			}

			@Override
			public Enumeration<String> getHeaders(String name) {

				if ("Authorization".equalsIgnoreCase(name)) {
					return Collections.emptyEnumeration();
				}
				if (USER_ID_IN_HEADER.equalsIgnoreCase(name)) {
					headerSet = new HashSet();
					headerSet.add(ANONYMOUS_USER_ID);
					return Collections.enumeration(headerSet);
				}
				return super.getHeaders(name);
			}

			@Override
			public String getHeader(String name) {
				if ("Authorization".equalsIgnoreCase(name)) {
					return null;
				}
				if (USER_ID_IN_HEADER.equalsIgnoreCase(name)) {
					return ANONYMOUS_USER_ID;
				}
				return super.getHeader(name);
			}
		};
		return httpServletRequestWrapper;
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
