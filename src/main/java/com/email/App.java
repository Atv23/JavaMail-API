package com.email;

import java.io.File;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import io.github.cdimascio.dotenv.Dotenv;

public class App {
	public static void main(String[] args) {

		System.out.println("preparing to send message ...");
		// 1. Create the content
		String message = "I hope this email finds you well. This email is sent to you through Java code!!";
		String subject = "Incoming Alert";
		String to = "atv23shukla@gmail.com";
		String from = "grouptask2024@gmail.com";
		// Importing username and password from .env file
		Dotenv dotenv = Dotenv.load();
		String username = dotenv.get("EMAIL_USERNAME");
		String password = dotenv.get("EMAIL_PASSWORD");

		if (username == null || password == null) {
			System.err.println("Email credentials are not set in environment variables.");
			return;
		}

		 sendEmail(message, subject, to, from, username, password);
//		 sendAttach(message,subject,to,from,username, password);
	}

	// Helper method to create a mail session
	private static Session createSession(String username, String password) {
		// 2. Configure SMTP Properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com"); // Address of SMTP server (gmail/yahoo/outlook,etc.)
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		session.setDebug(true); // For debugging
		return session;
	}

	// this is responsible to send the message with attachment
	private static void sendAttach(String message, String subject, String to, String from, String username,
			String password) {

		// 3. Create a Mail Session with Authentication
		Session session = createSession(username, password);

		// 4. Compose the email [text,multi-media]
		MimeMessage msg = new MimeMessage(session);

		try {
			// Sender's email
			msg.setFrom(from);
			// adding subject to message
			msg.setSubject(subject);
			// adding recipient to message
			InternetAddress recipientAddress = new InternetAddress(to);
			recipientAddress.validate();
			msg.addRecipient(Message.RecipientType.TO, recipientAddress);
			// Attachment...
			// file path
			String path = "A:\\IDs\\pics\\IMG_9712.JPG";
			File file = new File(path);
			if (!file.exists() || !file.isFile()) {
				System.err.println("Attachment file not found: " + path);
				return; 
			}

			MimeMultipart mimeMultipart = new MimeMultipart();
			// text & file
			MimeBodyPart textMime = new MimeBodyPart();
			MimeBodyPart fileMime = new MimeBodyPart();
			textMime.setText(message);
			fileMime.attachFile(file);
			mimeMultipart.addBodyPart(textMime);
			mimeMultipart.addBodyPart(fileMime);

			msg.setContent(mimeMultipart);

			// 5. Send the e-mail using Transport class
			Transport.send(msg);

		} catch (Exception e) {
			System.out.println("OOPS! Mail couldn't be sent, Error: " + e);
			e.printStackTrace();
		}

		System.out.println("Sent success...................");
	}

	// for sending plain text mail
	private static void sendEmail(String message, String subject, String to, String from, String username,
			String password) {

		// 3. Create a Mail Session with Authentication
		Session session = createSession(username, password);

		// 4. Compose the email [text,multi-media]
		MimeMessage msg = new MimeMessage(session);
		try {
			// adding sender's email
			msg.setFrom(from);
			// adding subject to message
			msg.setSubject(subject);
			// adding text to message
			msg.setText(message);
			// adding recipient to message
			InternetAddress recipientAddress = new InternetAddress(to);
			recipientAddress.validate();
			msg.addRecipient(Message.RecipientType.TO, recipientAddress);

			// 5. Send the e-mail using Transport class
			Transport.send(msg);
			System.out.println("Sent success...................");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("OOPS! Mail couldn't be sent, Error: " + e);
		}
	}
}