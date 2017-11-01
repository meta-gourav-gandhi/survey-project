package com.metacube.wesurve.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	public void sendEmail(String from, String password, String to, String subject, String body) {
		Message message;

		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.debug", "false");
		Session session = Session.getDefaultInstance(properties, new MailAuthenticator(from, password));

		try {
			message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
		} catch (MessagingException exception) {
			exception.printStackTrace();
		}
	}
}