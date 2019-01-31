package org.smart.gateway.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class LoginCheckFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		// TODO Auto-generated method stub
		if(SecurityContextHolder.getContext().getAuthentication()!=null){
			String uri = ((HttpServletRequest)arg0).getRequestURI();
			// 已登录，移除
			if(uri.endsWith("/login")){
				SecurityContextHolder.clearContext();
			}
		}
		arg2.doFilter(arg0, arg1);
	}	

}
