package org.smart.service.pojo;

import java.util.Map;

public class ResultMap {
	private String ec;
	private String em;
	private Map<String, Object>map;
	
	public ResultMap(String ec, String em, Map<String, Object> map) {
		super();
		this.ec = ec;
		this.em = em;
		this.map = map;
	}
	
	public String getEc() {
		return ec;
	}
	public void setEc(String ec) {
		this.ec = ec;
	}
	public String getEm() {
		return em;
	}
	public void setEm(String em) {
		this.em = em;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	
	
}
