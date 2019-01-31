package org.smart.gateway.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth.permitall")
public class PermitAllUrlProperties {

	 private static List<Pattern> permitallUrlPattern;

	    
	 	private String pattern;	 	
	    public String getPattern() {
			return pattern;
		}
		public void setPattern(String pattern) {
			this.pattern = pattern;
		}

		private List<String> permitall;
		
	    public String[] getPermitallPatterns() {	      
	    	return permitall.toArray(new String[0]);
	    }

	    public static List<Pattern> getPermitallUrlPattern() {
	        return permitallUrlPattern;
	    }

	  
	    @PostConstruct
	    public void init() {
	        if (pattern!=null){
	        	permitall = new ArrayList<>();
	        	this.permitallUrlPattern = new ArrayList();
	        	StringTokenizer stringTokenizer = new StringTokenizer(pattern,",");
	        	while(stringTokenizer.hasMoreElements()){
	        		String tmpString = stringTokenizer.nextToken();
	        		permitall.add(tmpString);
	        		String currentUrl =tmpString.replaceAll("\\*\\*", "(.*?)");
		            Pattern currentPattern = Pattern.compile(currentUrl, Pattern.CASE_INSENSITIVE);
		            permitallUrlPattern.add(currentPattern);
	        	}
	        }
	    }

	    public boolean isPermitAllUrl(String url) {
	        for (Pattern pattern : permitallUrlPattern) {
	            if (pattern.matcher(url).find()) {
	                return true;
	            }
	        }
	        return false;
	    }

}
