package com.upwork.example.service;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderService {
	@Autowired
	JavaMailSender mailSender;

	public void sendEmailWithTemplate(List<String> emailId, String msg, String subject) {
		String emailTemplate = msg;
		sendEmail(emailId, emailTemplate, subject);
	}
	public void sendEmail(List<String> emailId, String msgbody, String subject) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setSubject(subject);
			messageHelper.setTo(emailId.toArray(new String[0]));
			messageHelper.setFrom(new InternetAddress("devbev61@gmail.com"));
			messageHelper.setText(msgbody, true);
		} catch (MessagingException e1) {
			e1.printStackTrace();
		}
		try {
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
