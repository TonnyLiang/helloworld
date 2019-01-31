package org.smart.service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	@PostMapping(value="/login.do")
	public String login(@RequestParam(name="username") String userName, @RequestParam(name="password") String passWord){
		if(userName.equals("admin") && passWord.equals("admin")){
			System.out.println("smart --- service");
			return "success";
		}
		return "fail";
	}
}
