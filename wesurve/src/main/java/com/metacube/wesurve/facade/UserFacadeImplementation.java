package com.metacube.wesurve.facade;

import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metacube.wesurve.dto.UserDto;
import com.metacube.wesurve.enums.Status;
import com.metacube.wesurve.model.User;
import com.metacube.wesurve.service.UserService;

@Component("userFacade")
public class UserFacadeImplementation implements UserFacade {
	@Autowired
	UserService userService;

	public UserFacadeImplementation() {
	}

	@Override
	public Status createNewUser(UserDto userDto) {
		Status outputStatus = Status.FAILURE;
		if(validateUser(userDto)) {
			if(!userService.checkIfEmailExists(userDto.getEmail())) {
				User user = convertDtoToModel(userDto);
				if (userService.createNewUser(user) != null) {
					outputStatus = Status.SUCCESS;
				}
			} else {
				System.out.println("duplicate");
				outputStatus = Status.DUPLICATE;
			}
		} else {
			System.out.println("Failed");
		}
		return outputStatus;
	}

	private boolean validateUser(UserDto userDto) {
		boolean result = true;
		
		boolean condition1 = (userDto.getName() == null || userDto.getName().trim().isEmpty()) || 
				(userDto.getPassword() == null || userDto.getPassword().trim().isEmpty()) ||
				(userDto.getEmail() == null || userDto.getEmail().trim().isEmpty()) ||
				(userDto.getDob() == null);
		boolean condition2 = userDto.getPassword().length() >= 8;
		Pattern validEmailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = validEmailPattern.matcher(userDto.getEmail());
		boolean condition3 = matcher.find();
		
		if(condition1 == true || condition2 == false || condition3 == false) {
			result = false;
		}
		
		return result;
	}

	private User convertDtoToModel(UserDto userDto) {
		User user = new User();
		user.setName(userDto.getName());
		user.setDob(userDto.getDob());
		user.setEmail(userDto.getEmail());
		user.setGender(userDto.getGender());
		user.setPassword(userDto.getPassword());
		user.setCreatedDate(new Date());
		user.setUpdatedDate(new Date());
		return user;
	}
	
	public void sendEmail(String from, String password, String to, String subject, String body) {
		Message message;

		Properties properties = new Properties();
		properties.put("mail.smtp.host","smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.auth","true"); 
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.debug", "false");
		Session session = Session.getDefaultInstance(properties, new MailAuthenticator(from, password));
		
		try {
			message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			
			message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
		}
		catch(MessagingException exception) {
			exception.printStackTrace();
		}
	}
}