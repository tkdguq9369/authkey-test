package com.auth.authKeyTest.plugin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpSession;

public class AuthKeyPlugin {

	// email 인증키 만들기
	public String getAuthKey(int keyLength) {
		char[] keySet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
				'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < keyLength; i++) {
			int idx = (int) (keySet.length * Math.random());
			sb.append(keySet[idx]);
		}

		return sb.toString();
	}

	// 인증 시간 세션에 설정
	public void setAuthTime(HttpSession session) {

		String authTime = null;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.MINUTE, 3);
		authTime = sdf.format(cal.getTime());
 
		session.setAttribute("AUTHTIME", authTime);
		System.out.println("=====  set authTime  =====");
	}

	// 인증 확인 및 처리
	public String authChk(HttpSession session, String authKey) {

		String returnMsg = null;
		String sessionAuthKey = (String) session.getAttribute("AUTHKEY");
		String sessionAuthTime = (String) session.getAttribute("AUTHTIME");

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		// session이 null이 아닌경우만 try catch 진행
		if (sessionAuthTime != null) {
			try {
				// 현재시간
				Date currentDate = new Date();

				// 세션에 저장된 시간
				Date sessionDate = sdf.parse(sessionAuthTime);

				if (currentDate.getTime() > sessionDate.getTime()) {
					// currentDate가 큰것은 인증시간을 지난 상태. 인증키, 인증시간
					returnMsg = "FAIL";
				} else if (currentDate.getTime() < sessionDate.getTime()) {
					// sessionDate가 큰것은 인증시간이 아직 남은 상태.

					if (sessionAuthKey.equals(authKey)) {
						// 인증시간이 남은 상태에서 session authkey와 일치하는 경우
						returnMsg = "OK";
					} else {
						// 인증시간이 남은 상태에서 session authkey 일치하지 않는 경우
						returnMsg = "FAIL";
					}

				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			returnMsg = "FAIL";
		}

		if ("OK".equals(returnMsg)) {
			session.removeAttribute("AUTHTIME");
		}

		System.out.println("=====  authTime Check  =====");
		return returnMsg;

	}

}
