package com.auth.authKeyTest.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.auth.authKeyTest.plugin.AuthKeyPlugin;

@Service
public class MailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private HttpSession session;

	AuthKeyPlugin authPlugin = new AuthKeyPlugin();

	public String sendMailProc(String userId) throws UnsupportedEncodingException {
		boolean isSend = false;
		MimeMessage msg = javaMailSender.createMimeMessage();

		// 인증키 생성
		String authKey = authPlugin.getAuthKey(6);

		try {
			msg.setSubject("이메일 인증을 확인해주세요.", "UTF-8");
			String htmlContent = "<div style=\"background:#f4f4f4; padding:30px;\">\r\n"
					+ "	<div style=\"border:1px solid #ddd; background:#fff;\">\r\n"
					+ "		<h1 style=\"line-height:1; margin:0; padding:55px 35px; border-bottom:3px double #ddd;\"><strong style=\"display:block; color:#C90; padding-bottom:10px;\">TEST</strong> 인증번호 메일 </h1>\r\n"
					+ "		<div style=\"padding:40px 35px;\">\r\n"
					+ "			<h2 style=\"margin:0; color:#444; border-bottom:2px solid #444; padding-bottom:20px;\">인증키 발급 정보</h2>\r\n"
					+ "			<table style=\"border-collapse:collapse; line-height:2; width:100%;\">\r\n"
					+ "				<tbody>\r\n" + "					<tr>\r\n"
					+ "						<th colspan=\"6\" style=\"border-top:1px dashed #ddd; padding:20px; background:#fafafa; color:#aaa;\">인증키</th>\r\n"
					+ "					</tr>\r\n" + "					<tr>\r\n"
					+ "						<td colspan=\"6\" style=\"background:#fafafa; padding:0 40px 40px 40px; border-bottom:1px solid #ddd;\">"
					+ authKey + "</td>\r\n" + "					</tr>\r\n" + "				</tbody>\r\n"
					+ "			</table>\r\n" + "		</div>\r\n" + "	</div>\r\n" + "</div>";
			msg.setText(htmlContent, "UTF-8", "html");

			// 보내는 사람
			msg.setFrom(new InternetAddress("보내는사람 메일", "인증키 관리자"));

			// 받는사람 (추후 userId로 변경할것)
			msg.addRecipient(RecipientType.TO, new InternetAddress("받는사람 메일"));

			javaMailSender.send(msg);
			isSend = true;
			session.setAttribute("AUTHKEY", authKey);

			// 세션에 현재시간 +3분을 인증시간으로 저장
			authPlugin.setAuthTime(session);

		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (MailException e) {
			e.printStackTrace();
		}

		if (isSend) {
			return "OK";
		} else {
			return "FAIL";
		}

	}

	public String authChkProc(String authKey) {
		String chk = authPlugin.authChk(session, authKey);
		return chk;
	}

	public String authTimerDelProc() {

		String authTime = (String) session.getAttribute("AUTHTIME");
		String result = null;

		if (!"".equals(authTime)) {
			session.removeAttribute("AUTHTIME");
			result = "OK";
		}

		return result;

	}

}
