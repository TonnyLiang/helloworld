package org.smart.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class AccessFilter extends ZuulFilter {
	private static final Logger logger = LoggerFactory.getLogger(AccessFilter.class); 
	
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();		
		if(ctx.getRequest().getSession(false) != null){
			String session_id = "session_id:"+ctx.getRequest().getSession().getId();
			logger.info("org.smart.gateway.filter.AccessFilter-session_id: "+session_id);
			RequestContext.getCurrentContext().addZuulRequestHeader("SESSION-ID", session_id);
		}
		return null;
	}

	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}

}
