package com.rally.psw.bdnotify;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
	private final String username = "user@gmail.com";
	private final String password = "user-password";
	private String sendTo = "sendTo@gmail.com";
	
	private Properties props;
	
	private Session session;
	
	public SendEmail(String sendTo) {
		this.sendTo = sendTo;
		props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}
	
	public void sendMail() {
		
		try {
			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(username));

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					sendTo));

			message.setSubject("Your burndown is not burning well enough. Huh!!!");

			message.setText("The burndown is all messed up. Kindly set it right ASAP");

			Transport.send(message);

			System.out.println("Sent message successfully ....");

		} catch (MessagingException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
