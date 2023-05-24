package com.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailSenderService {

	@Autowired
	JavaMailSender mailSender;
	
	public String sendEmail(String toEmail,String subject,String body) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom("vishalchinnu25@gmail.com");
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);
		try {
			mailSender.send(message);
			return "success";
		}
		catch(Exception e) {
			return "Error occured in sending mail....";
		}
		
	}
}
