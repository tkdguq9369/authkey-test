package com.auth.authKeyTest.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth.authKeyTest.service.MailService;

@RestController
@RequestMapping("/api/mail")
public class MailRestController {

	@Autowired
	private MailService mailService;

	// 인증번호 메일 보내기 OK or FAIL
	@RequestMapping("/send")
	public String sendMail(@RequestParam(value = "userId") String userId) throws UnsupportedEncodingException {
		return mailService.sendMailProc(userId);
	}

	// 인증시간, 인증키 유효 검사. OK or FAIL
	@RequestMapping("/authChk")
	public String mailAuthChk(@RequestParam(value = "authKey") String authKey) {
		return mailService.authChkProc(authKey);
	}

	// 인증 타이머 3분 지나면 인증키를 세션에서 제거
	@RequestMapping("/authTimer")
	public String authTimerDel() {
		return mailService.authTimerDelProc();
	}

}
