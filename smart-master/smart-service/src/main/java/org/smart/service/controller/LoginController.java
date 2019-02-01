package org.smart.service.controller;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.smart.service.pojo.UserDetail;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	/*@GetMapping(value="/login.do")
	public String login(@RequestParam(name="username") String userName, @RequestParam(name="password") String passWord){
		if(userName.equals("admin") && passWord.equals("admin")){
			System.out.println("smart --- service");
			return "success";
		}
		return "fail";
	}*/
	
	@RequestMapping(value="/login.do",method=RequestMethod.POST)
	public Map<String, Object> login(@RequestBody Map<String,Object> map){
		
		System.out.println("smart --- service");
				Map map2 = new HashedMap();
		map2.put("ec", "0");
		map2.put("em", map.get("username"));
		map2.put("data", new UserDetail((String)map.get("userId")));
		return map2;
	}
}
