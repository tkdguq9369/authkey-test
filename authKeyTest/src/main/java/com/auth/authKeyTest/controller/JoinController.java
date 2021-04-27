package com.auth.authKeyTest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/join")
public class JoinController {

	@RequestMapping("/joinForm")
	public String joinForm() {
		
		return "join/joinForm";
	}
}
